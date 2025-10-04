package com.eipl.amcs.operation.procurement.dto;

import com.eipl.amcs.master.inventory.model.Product;
import com.eipl.amcs.master.inventory.model.ProductSaleRate;


public class ProductAndSaleRateDto {
	public ProductAndSaleRateDto() {
	}

	public Product product;
	public ProductSaleRate productSaleRate;

	public ProductAndSaleRateDto(Product product, ProductSaleRate productSaleRate) {
		this.product = product;
		this.productSaleRate = productSaleRate;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public ProductSaleRate getProductSaleRate() {
		return productSaleRate;
	}

	public void setProductSaleRate(ProductSaleRate productSaleRate) {
		this.productSaleRate = productSaleRate;
	}
}
