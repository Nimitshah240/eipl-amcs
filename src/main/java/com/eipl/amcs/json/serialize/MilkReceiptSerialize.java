package com.eipl.amcs.json.serialize;

import com.eipl.amcs.operation.procurement.model.MilkReceipt;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class MilkReceiptSerialize extends JsonSerializer<MilkReceipt> {
    @Override
    public void serialize(MilkReceipt milkReceipt, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(milkReceipt.getCode());
    }
}
