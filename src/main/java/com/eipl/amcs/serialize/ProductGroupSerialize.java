package com.eipl.amcs.serialize;

import com.eipl.amcs.master.inventory.model.ProductGroup;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class ProductGroupSerialize extends JsonSerializer<ProductGroup> {
    @Override
    public void serialize(ProductGroup productGroup, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeNumber(productGroup.getCode());
    }
}
