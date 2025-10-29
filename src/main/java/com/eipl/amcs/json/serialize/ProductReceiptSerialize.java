package com.eipl.amcs.json.serialize;

import com.eipl.amcs.operation.inventory.model.ProductReceipt;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class ProductReceiptSerialize extends JsonSerializer<ProductReceipt> {
    @Override
    public void serialize(ProductReceipt productReceipt, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(productReceipt.getGrnNo());
    }
}
