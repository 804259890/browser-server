package com.platon.browser.task;

import com.platon.browser.TestBase;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dto.CustomStaking;
import com.platon.browser.engine.cache.CacheHolder;
import com.platon.browser.engine.cache.ProposalCache;
import com.platon.browser.engine.stage.BlockChainStage;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.*;

/**
 * @Auther: Chendongming
 * @Date: 2019/9/9 20:29
 * @Description:
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class StakingUpdateTaskTest extends TestBase {
    @Spy
    private StakingUpdateTask stakingUpdateTask;
    @Mock
    private BlockChainConfig chainConfig;
    @Mock
    private CacheHolder cacheHolder;

    @Before
    public void setup(){
        ReflectionTestUtils.setField(stakingUpdateTask, "chainConfig", chainConfig);
        ReflectionTestUtils.setField(stakingUpdateTask, "cacheHolder", cacheHolder);
    }

    @Test
    public void testStart() {
        BlockChainStage stageData = new BlockChainStage();
        when(cacheHolder.getStageData()).thenReturn(stageData);
        when(chainConfig.getKeyBase()).thenReturn("https://keybase.io/");
        Set<CustomStaking> customStakingSet = new HashSet<>(stakings);
        customStakingSet.forEach(cc->{
            cc.setExternalName("");
            cc.setStakingIcon("");
            cc.setExternalId("2A0CD8DCB4BF2BE0");
        });
        doReturn(customStakingSet).when(stakingUpdateTask).getAllStaking();
        stakingUpdateTask.start();
        verify(stakingUpdateTask, times(1)).start();
    }
}