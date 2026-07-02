package com.eipl.amcs.report.dto;

import java.math.BigDecimal;
import java.math.BigInteger;

public interface PaymentForBankProjection {

    BigInteger getSr_no();
    String getSoc_code();
    String getSoc_name();
    String getPayment_period();
    String getMember_code();
    String getMember_name();
    String getBank_acno();
    String getIfsc();
    String getBank_name();
    String getBranch_name();
    BigDecimal getNet_amount();
    BigDecimal getMilk_amount();
    BigDecimal getOther_ded_amount();
}