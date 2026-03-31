package com.eipl.amcs.setting.dto;

import com.eipl.amcs.master.account.model.SubLedger;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CustomerTypeWiseTotalDto {
    private String customerCode;
    private String customerType;
    private BigDecimal amount;
    private SubLedger subLedger;
}
