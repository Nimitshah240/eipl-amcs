package com.eipl.amcs.json.serialize;

import com.eipl.amcs.operation.inventory.model.ProductDispatch;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class ProductDispatchSerialize extends JsonSerializer<ProductDispatch> {
    @Override
    public void serialize(ProductDispatch productDispatch, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(productDispatch.getChallanNo());
    }
}
