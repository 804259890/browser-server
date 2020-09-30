package com.platon.browser.complement.converter.epoch;

import com.platon.browser.AgentTestBase;
import com.platon.browser.client.HistoryLowRateSlash;
import com.platon.browser.client.PlatOnClient;
import com.platon.browser.client.SpecialApi;
import com.platon.browser.client.Web3jWrapper;
import com.platon.browser.common.collection.dto.EpochMessage;
import com.platon.browser.common.complement.cache.NetworkStatCache;
import com.platon.browser.common.queue.collection.event.CollectionEvent;
import com.platon.browser.complement.dao.mapper.EpochBusinessMapper;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dao.entity.Staking;
import com.platon.browser.dao.mapper.StakingMapper;
import com.platon.browser.dto.CustomStaking.StatusEnum;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.exception.BlockNumberException;
import com.platon.browser.service.misc.StakeMiscService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;
import com.alaya.protocol.Web3j;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class OnElectionConverterTest extends AgentTestBase {

    @Mock
    private EpochBusinessMapper epochBusinessMapper;
    @Mock
    private NetworkStatCache networkStatCache;
    @Mock
    private SpecialApi specialApi;
    @Mock
    private PlatOnClient platOnClient;
    @Mock
    private StakingMapper stakingMapper;
    @Mock
    private StakeMiscService stakeMiscService;
    @Mock
    private BlockChainConfig chainConfig;

    @Spy
    private OnElectionConverter target;

    @Before
    public void setup() throws Exception {
        ReflectionTestUtils.setField(this.target, "epochBusinessMapper", this.epochBusinessMapper);
        ReflectionTestUtils.setField(this.target, "networkStatCache", this.networkStatCache);
        ReflectionTestUtils.setField(this.target, "specialApi", this.specialApi);
        ReflectionTestUtils.setField(this.target, "platOnClient", this.platOnClient);
        ReflectionTestUtils.setField(this.target, "stakingMapper", this.stakingMapper);
        ReflectionTestUtils.setField(this.target, "chainConfig", this.chainConfig);
        ReflectionTestUtils.setField(this.target, "stakeMiscService", this.stakeMiscService);
        List<Staking> list = new ArrayList<>();
        for (int i = 0; i < this.stakingList.size(); i++) {
            Staking staking = new Staking();
            staking.setNodeId(this.stakingList.get(i).getNodeId());
            staking.setStakingBlockNum(this.stakingList.get(i).getStakingBlockNum());
            staking.setStakingHes(BigDecimal.ONE);
            staking.setStatDelegateHes(BigDecimal.ONE);
            staking.setStatDelegateLocked(BigDecimal.ONE);
            staking.setStatDelegateReleased(BigDecimal.ONE);
            staking.setStakingLocked(BigDecimal.ONE);
            staking.setStakingReduction(BigDecimal.ONE);
            staking.setStatus(StatusEnum.EXITING.getCode());
            staking.setUnStakeFreezeDuration(1);
            staking.setUnStakeEndBlock(1l);
            if (i == 1) {
                staking.setStatus(StatusEnum.CANDIDATE.getCode());
            } else {
                staking.setStakingReduction(new BigDecimal("-1"));
            }
            staking.setLowRateSlashCount(0);
            list.add(staking);
        }

        List<HistoryLowRateSlash> list1 = new ArrayList<>();
        HistoryLowRateSlash historyLowRateSlash = new HistoryLowRateSlash();
        historyLowRateSlash.setNodeId("0x");
        historyLowRateSlash.setAmount(BigInteger.ZERO);
        list1.add(historyLowRateSlash);
        when(this.specialApi.getHistoryLowRateSlashList(any(), any())).thenReturn(list1);
        when(this.stakingMapper.selectByExample(any())).thenReturn(list);
        when(this.networkStatCache.getAndIncrementNodeOptSeq()).thenReturn(1l);
        when(this.chainConfig.getConsensusPeriodBlockCount()).thenReturn(BigInteger.TEN);
        when(this.chainConfig.getSettlePeriodBlockCount()).thenReturn(BigInteger.TEN);
        when(this.chainConfig.getSlashBlockRewardCount()).thenReturn(BigDecimal.TEN);
        when(this.chainConfig.getSlashBlockRewardCount()).thenReturn(BigDecimal.TEN);
        when(this.chainConfig.getStakeThreshold()).thenReturn(BigDecimal.TEN);
        when(this.stakeMiscService.getUnStakeEndBlock(anyString(), any(BigInteger.class), anyBoolean()))
            .thenReturn(BigInteger.TEN);
        when(this.stakeMiscService.getUnStakeFreeDuration()).thenReturn(BigInteger.TEN);
        when(this.stakeMiscService.getZeroProduceFreeDuration()).thenReturn(BigInteger.TEN);
        Web3jWrapper web3jWrapper = mock(Web3jWrapper.class);
        when(this.platOnClient.getWeb3jWrapper()).thenReturn(web3jWrapper);
        Web3j web3j = mock(Web3j.class);
        when(web3jWrapper.getWeb3j()).thenReturn(web3j);
    }

    @Test
    public void convert() throws BlockNumberException {
        Block block = this.blockList.get(0);
        EpochMessage epochMessage = EpochMessage.newInstance();
        epochMessage.setPreValidatorList(this.validatorList);
        epochMessage.setSettleEpochRound(BigInteger.TEN);

        CollectionEvent collectionEvent = new CollectionEvent();
        collectionEvent.setBlock(block);
        collectionEvent.setEpochMessage(epochMessage);
        this.target.convert(collectionEvent, block);
        when(this.stakingMapper.selectByExample(any())).thenReturn(Collections.EMPTY_LIST);
        this.target.convert(collectionEvent, block);
        try {
            when(this.stakingMapper.selectByExample(any())).thenReturn(null);
            this.target.convert(collectionEvent, block);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

}
