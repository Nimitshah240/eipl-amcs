package com.eipl.amcs.operation.inventory.dto;

import com.eipl.amcs.master.account.dto.TaxDto;
import com.eipl.amcs.master.global.model.Unit;
import com.eipl.amcs.master.inventory.model.Product;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class SaleTxnDto {
    private LocalDate date;
    private List<Product> productList;
    private List<Unit> unitList;
    private List<TaxDto> taxDtoList;
}
