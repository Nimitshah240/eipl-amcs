package com.eipl.amcs.serialize;

import com.eipl.amcs.operation.inventory.model.ProductRequisition;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class ProductRequisitionSerialize extends JsonSerializer<ProductRequisition> {
    @Override
    public void serialize(ProductRequisition productRequisition, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(productRequisition.getCode());
    }
}
