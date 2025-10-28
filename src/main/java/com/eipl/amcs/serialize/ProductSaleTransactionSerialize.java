package com.eipl.amcs.serialize;

import com.eipl.amcs.operation.inventory.model.ProductSaleTransaction;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class ProductSaleTransactionSerialize extends JsonSerializer<ProductSaleTransaction> {
    @Override
    public void serialize(ProductSaleTransaction productSaleTransaction, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(productSaleTransaction.getInvoiceTxnNo());
    }
}
