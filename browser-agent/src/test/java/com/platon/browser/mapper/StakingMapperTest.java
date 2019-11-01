package com.platon.browser.mapper;

import com.platon.browser.AgentApplication;
import com.platon.browser.TestBase;
import com.platon.browser.dao.entity.Staking;
import com.platon.browser.dao.entity.StakingExample;
import com.platon.browser.dao.entity.StakingKey;
import com.platon.browser.dao.mapper.NodeMapper;
import com.platon.browser.dao.mapper.NodeOptMapper;
import com.platon.browser.dao.mapper.StakingMapper;
import com.platon.browser.mapper.ParamBuild.ParamBuildTest;
import com.platon.browser.persistence.dao.mapper.CreateStakingMapper;
import com.platon.browser.persistence.dao.mapper.ModifyStakingMapper;
import com.platon.browser.persistence.dao.param.CreateStakingParam;
import com.platon.browser.persistence.dao.param.ModifyStakingParam;
import org.checkerframework.checker.units.qual.A;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @Auther: dongqile
 * @Date: 2019/10/31
 * @Description:
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes= AgentApplication.class, value = "spring.profiles.active=test")
@SpringBootApplication
public class StakingMapperTest extends TestBase {

    @Autowired
    private CreateStakingMapper createStakingMapper;

    @Autowired
    private ModifyStakingMapper modifyStakingMapper;

    @Autowired
    private ParamBuildTest paramBuildTest;

    @Autowired
    private NodeOptMapper nodeOptMapper;

    @Autowired
    private StakingMapper stakingMapper;

    @Autowired
    private NodeMapper nodeMapper;

    @Test
    public void createStakingMapper(){
        CreateStakingParam createStakingParam = paramBuildTest.crateStakingBuild();
        createStakingMapper.createStaking(createStakingParam);
        StakingKey stakingKey = new StakingKey();
        stakingKey.setNodeId(createStakingParam.getNodeId());
        stakingKey.setStakingBlockNum(createStakingParam.getStakingBlockNum().longValue());
        Staking staking = stakingMapper.selectByPrimaryKey(stakingKey);
        assertEquals(createStakingParam.getNodeId(), staking.getNodeId());

    }

    @Test
    public void modifyStakingMapper(){
        ModifyStakingParam modifyStakingParam = new ModifyStakingParam();
        modifyStakingParam.setNodeName("testNode02");
        modifyStakingParam.setExternalId("externalId02");
        modifyStakingParam.setBenefitAddr("0xff48d9712d8a55bf603dab28f4645b6985696a61");
        modifyStakingParam.setWebSite("web_site01");
        modifyStakingParam.setDetails("details01");
        modifyStakingParam.setIsInit(2);
        modifyStakingParam.setTxHash("0xaa85c7e85542ac8e8d2428c618130d02723138437d105d06d405f9e735469be7");
        modifyStakingParam.setStakingBlockNum(new BigInteger("200"));
        modifyStakingParam.setBNum(new BigInteger("300"));
        modifyStakingParam.setTime(new Date(System.currentTimeMillis()));
        modifyStakingParam.setNodeId("0x20a090d94bc5015c9339a46e9ca5d80057a5ef25cc14e71cef67b502ec32949253f046821e80dfb6ff666ef0e0badf58fdb719368c38393f7c40ebcf18d8ed18");
        modifyStakingMapper.modifyStaking(modifyStakingParam);
    }
}