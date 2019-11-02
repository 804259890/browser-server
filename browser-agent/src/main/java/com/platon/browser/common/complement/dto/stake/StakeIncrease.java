package com.platon.browser.common.complement.dto.stake;

import com.platon.browser.common.complement.dto.BusinessParam;
import com.platon.browser.common.enums.BusinessType;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

@Data
@Slf4j
@Builder
@Accessors(chain = true)
public class StakeIncrease extends BusinessParam {
    private String nodeId;
    private BigDecimal amount;
    private String Hash;
    private BigInteger bNum;
    private BigInteger stakingBlockNum;
    private Date time;

    @Override
    public BusinessType getBusinessType() {
        return BusinessType.STAKE_INCREASE;
    }
}
