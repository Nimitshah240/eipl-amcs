package com.eipl.amcs.serialize;

import com.eipl.amcs.operation.inventory.model.ProductRequisitionTransaction;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class ProductRequisitionTransactionSerialize extends JsonSerializer<ProductRequisitionTransaction> {
    @Override
    public void serialize(ProductRequisitionTransaction productRequisitionTransaction, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(productRequisitionTransaction.getCode());
    }
}
