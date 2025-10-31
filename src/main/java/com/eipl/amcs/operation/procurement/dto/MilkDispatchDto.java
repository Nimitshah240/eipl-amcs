package com.eipl.amcs.operation.procurement.dto;

import com.eipl.amcs.operation.procurement.model.MilkDispatch;
import com.eipl.amcs.operation.procurement.model.MilkDispatchTransaction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MilkDispatchDto {
    private MilkDispatch milkDispatch;
    private List<MilkDispatchTransaction> milkDispatchTransaction;
}
