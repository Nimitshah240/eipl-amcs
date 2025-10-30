package com.eipl.amcs.operation.procurement.dto;

import com.eipl.amcs.operation.procurement.model.MilkCollection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class MemberWiseCollectionDto {
    private Map<String, BigDecimal> avg;
    private Map<String, BigDecimal> total;
    private List<MilkCollection> collectionList;
}
