package com.platon.browser.complement.converter.stake;

import com.platon.browser.common.complement.cache.NetworkStatCache;
import com.platon.browser.common.complement.dto.ComplementNodeOpt;
import com.platon.browser.common.queue.collection.event.CollectionEvent;
import com.platon.browser.complement.converter.BusinessParamConverter;
import com.platon.browser.complement.dao.mapper.StakeBusinessMapper;
import com.platon.browser.complement.dao.param.stake.StakeExit;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dao.entity.Proposal;
import com.platon.browser.dao.entity.ProposalExample;
import com.platon.browser.dao.entity.Staking;
import com.platon.browser.dao.entity.StakingKey;
import com.platon.browser.dao.mapper.ProposalMapper;
import com.platon.browser.dao.mapper.StakingMapper;
import com.platon.browser.dto.CustomProposal;
import com.platon.browser.elasticsearch.dto.NodeOpt;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.enums.ModifiableGovernParamEnum;
import com.platon.browser.exception.BlockNumberException;
import com.platon.browser.exception.BusinessException;
import com.platon.browser.param.StakeExitParam;
import com.platon.browser.service.govern.ParameterService;
import com.platon.browser.utils.EpochUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

/**
 * @description: 退出质押业务参数转换器
 * @author: chendongming@juzix.net
 * @create: 2019-11-04 17:58:27
 **/
@Slf4j
@Service
public class StakeExitConverter extends BusinessParamConverter<NodeOpt> {

    @Autowired
    private StakeBusinessMapper stakeBusinessMapper;
    @Autowired
    private NetworkStatCache networkStatCache;
    @Autowired
    private StakingMapper stakingMapper;
    @Autowired
    private BlockChainConfig chainConfig;
    @Autowired
    private ParameterService parameterService;
    @Autowired
    private ProposalMapper proposalMapper;
	
    @Override
    public NodeOpt convert(CollectionEvent event, Transaction tx) {
        // 撤销质押
        StakeExitParam txParam = tx.getTxParam(StakeExitParam.class);
        // 补充节点名称
        updateTxInfo(txParam,tx);
        // 失败的交易不分析业务数据
        if(Transaction.StatusEnum.FAILURE.getCode()==tx.getStatus()) return null;

        StakingKey stakingKey = new StakingKey();
        stakingKey.setNodeId(txParam.getNodeId());
        stakingKey.setStakingBlockNum(txParam.getStakingBlockNum().longValue());
        Staking staking = stakingMapper.selectByPrimaryKey(stakingKey);
        if(staking==null){
            throw new BusinessException("节点ID为["+txParam.getNodeId()+"],质押区块号为["+txParam.getStakingBlockNum()+"]的质押记录不存在！");
        }

        try {
            // 计算当前周期
            BigInteger curEpoch = EpochUtil.getEpoch(BigInteger.valueOf(tx.getNum()),chainConfig.getSettlePeriodBlockCount());
            // 计算质押金退回的区块号 = 每个结算周期的区块数x(退出质押所在结算周期轮数+需要经过的结算周期轮数)
            BigInteger withdrawBlockNum = chainConfig.getSettlePeriodBlockCount()
                    .multiply(curEpoch.add(chainConfig.getUnStakeRefundSettlePeriodCount()));
            txParam.setWithdrawBlockNum(withdrawBlockNum);
        } catch (BlockNumberException e) {
            log.error("",e);
            throw new BusinessException("周期计算错误!");
        }

        long startTime = System.currentTimeMillis();

        // 更新解质押到账需要经过的结算周期数
        String configVal = parameterService.getValueInBlockChainConfig(ModifiableGovernParamEnum.UN_STAKE_FREEZE_DURATION.getName());
        if(StringUtils.isBlank(configVal)){
            throw new BusinessException("参数表参数缺失："+ModifiableGovernParamEnum.UN_STAKE_FREEZE_DURATION.getName());
        }
        Integer  unStakeFreezeDuration = Integer.parseInt(configVal);
        // 理论上的退出区块号, 实际的退出块号还要跟状态为进行中的提案的投票截至区块进行对比，取最大者
        BigInteger unStakeEndBlock = event.getEpochMessage()
                .getSettleEpochRound() // 当前块所处的结算周期轮数
                .add(BigInteger.valueOf(unStakeFreezeDuration)) //+ 解质押需要经过的结算周期轮数
                .multiply(chainConfig.getSettlePeriodBlockCount()); // x 每个结算周期的区块数
        ProposalExample condition = new ProposalExample();
        condition.createCriteria()
                .andNodeIdEqualTo(txParam.getNodeId()) // 提案节点ID等于当前退出节点
                .andStatusEqualTo(CustomProposal.StatusEnum.VOTING.getCode()); // 提案状态为投票中
        List<Proposal> proposalList = proposalMapper.selectByExample(condition);
        for (Proposal proposal : proposalList) {
            BigInteger endVotingBlock = BigInteger.valueOf(proposal.getEndVotingBlock());
            if(endVotingBlock.compareTo(unStakeEndBlock)>0){
                // 如果提案中的截至块号大于当前质押理论上的退出块号，则使用提案的截至块号
                unStakeEndBlock = endVotingBlock;
            }
        }
        // 撤销质押
        StakeExit businessParam= StakeExit.builder()
        		.nodeId(txParam.getNodeId())
        		.stakingBlockNum(txParam.getStakingBlockNum())
        		.time(tx.getTime())
                .stakingReductionEpoch(event.getEpochMessage().getSettleEpochRound().intValue())
                .unStakeFreezeDuration(unStakeFreezeDuration)
                .unStakeEndBlock(unStakeEndBlock)
                .build();
        
        // 查询质押金额
        BigDecimal stakingValue = staking.getStakingHes().add(staking.getStakingLocked());
        
        // 质押撤销
        stakeBusinessMapper.exit(businessParam);
        
        // 补充txInfo
        txParam.setAmount(stakingValue);
        tx.setInfo(txParam.toJSONString());
    
        log.debug("处理耗时:{} ms",System.currentTimeMillis()-startTime);

        NodeOpt nodeOpt = ComplementNodeOpt.newInstance();
        nodeOpt.setId(networkStatCache.getAndIncrementNodeOptSeq());
        nodeOpt.setNodeId(txParam.getNodeId());
        nodeOpt.setType(Integer.valueOf(NodeOpt.TypeEnum.QUIT.getCode()));
        nodeOpt.setTxHash(tx.getHash());
        nodeOpt.setBNum(tx.getNum());
        nodeOpt.setTime(tx.getTime());
        return nodeOpt;
    }
}
