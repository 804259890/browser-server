package com.platon.browser.complement.service.param.converter;

import com.platon.browser.common.complement.cache.NetworkStatCache;
import com.platon.browser.common.complement.dto.ComplementNodeOpt;
import com.platon.browser.complement.dao.param.proposal.ProposalCancel;
import com.platon.browser.common.queue.collection.event.CollectionEvent;
import com.platon.browser.complement.dao.mapper.ProposalBusinessMapper;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dto.CustomNodeOpt;
import com.platon.browser.dto.CustomProposal;
import com.platon.browser.elasticsearch.dto.NodeOpt;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.param.ProposalCancelParam;
import com.platon.browser.util.RoundCalculation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Optional;

/**
 * @description: 委托业务参数转换器
 * @author: chendongming@juzix.net
 * @create: 2019-11-04 17:58:27
 **/
@Service
public class ProposalCancelConverter extends BusinessParamConverter<Optional<NodeOpt>> {

    @Autowired
    private BlockChainConfig chainConfig;
    @Autowired
    private ProposalBusinessMapper proposalBusinessMapper;
    @Autowired
    private NetworkStatCache networkStatCache;
	
    @Override
    public Optional<NodeOpt> convert(CollectionEvent event, Transaction tx) {
    	ProposalCancelParam txParam = tx.getTxParam(ProposalCancelParam.class);

    	ProposalCancel businessParam= ProposalCancel.builder()
    			.nodeId(txParam.getVerifier())
    			.pIDID(txParam.getPIDID())
    			.url(String.format(chainConfig.getProposalUrlTemplate(), txParam.getPIDID()))
    			.pipNum(String.format(chainConfig.getProposalPipNumTemplate(), txParam.getPIDID()))
    			.endVotingBlock(RoundCalculation.endBlockNumCal(tx.getNum().toString(),chainConfig.getProposalTextConsensusRounds(),chainConfig).toBigInteger())
    			.topic(CustomProposal.QUERY_FLAG)
    			.description(CustomProposal.QUERY_FLAG)
    			.txHash(tx.getHash())
    			.blockNumber(BigInteger.valueOf(tx.getNum()))
    			.timestamp(tx.getTime())
    			.stakingName(txParam.getNodeName())
    			.canceledId(txParam.getCanceledProposalID())
                .build();
    	
    	String desc = CustomNodeOpt.TypeEnum.PROPOSALS.getTpl()
				.replace("ID",txParam.getPIDID())
				.replace("TITLE",businessParam.getTopic())
				.replace("TYPE",CustomProposal.TypeEnum.CANCEL.getCode())
				.replace("VERSION","");
 
    	proposalBusinessMapper.cancel(businessParam);

		NodeOpt nodeOpt = ComplementNodeOpt.newInstance();
		nodeOpt.setId(networkStatCache.getAndIncrementNodeOptSeq());
		nodeOpt.setNodeId(txParam.getVerifier());
		nodeOpt.setType(Integer.valueOf(CustomNodeOpt.TypeEnum.PROPOSALS.getCode()));
		nodeOpt.setDesc(desc);
		nodeOpt.setTxHash(tx.getHash());
		nodeOpt.setBNum(event.getBlock().getNum());
		nodeOpt.setTime(event.getBlock().getTime());

        return Optional.ofNullable(nodeOpt);
    }
}