package com.eipl.amcs.serialize;

import com.eipl.amcs.operation.inventory.model.ProductSale;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class ProductSaleSerialize extends JsonSerializer<ProductSale> {
    @Override
    public void serialize(ProductSale productSale, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(productSale.getInvoiceNo());
    }
}
