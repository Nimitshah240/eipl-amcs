package com.eipl.amcs.operation.procurement.dto;

import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.operation.procurement.model.MilkCollection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@Setter
@Getter
public class MemberSocietyInfoDto {
    private Member member;
    private List<MilkCollection> memberCollection;
    private List<MilkCollection> prevCollectionData;
    private List<MilkCollection> currentPaymentCycleData;
    private BigDecimal fat;
    private BigDecimal snf;
    private BigDecimal clr;
    private BigDecimal qty;
}
