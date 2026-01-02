package com.eipl.amcs.operation.billing.dto;

import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.operation.model.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class MemberSummaryDto {

   private String memberCode;
   private Integer milkTypeCode;
   private BigDecimal totalQty;
   private BigDecimal totalAmount;

   public MemberSummaryDto(String memberCode, Integer milkTypeCode, BigDecimal totalQty, BigDecimal totalAmount) {
        this.memberCode = memberCode;
        this.milkTypeCode = milkTypeCode;
        this.totalQty = totalQty;
        this.totalAmount = totalAmount;
   }
}
