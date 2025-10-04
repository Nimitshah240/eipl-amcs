package com.eipl.amcs.master.inventory.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@SuppressWarnings("serial")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductAndSaleRateDto {
	public Product product;
	public ProductSaleRate productSaleRate;
}
