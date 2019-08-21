package com.platon.browser.dto;

import com.platon.browser.dao.entity.NetworkStat;
import jnr.ffi.annotations.In;

/**
 * User: dongqile
 * Date: 2019/8/20
 * Time: 11:13
 */
public class CustomNetworkStat extends NetworkStat {

    public CustomNetworkStat () {
        this.setCurrentNumber(0L);
        this.setNodeName("");
        this.setNodeId("");
        this.setTxQty(0);
        this.setCurrentTps(0);
        this.setMaxTps(0);
        this.setIssueValue("0");
        this.setTurnValue("0");
        this.setStakingDelegationValue("0");
        this.setStakingValue("0");
        this.setProposalQty(0);
        this.setDoingProposalQty(0);
        this.setAddressQty(0);
        this.setBlockReward("0");
        this.setStakingReward("0");
        this.setAddIssueBegin(0L);
        this.setAddIssueEnd(0L);
        this.setNextSetting(0L);
    }

}