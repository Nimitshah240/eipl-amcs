package com.eipl.amcs.serialize;

import com.eipl.amcs.master.operation.model.BillHead;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class BillHeadSerialize extends JsonSerializer<BillHead> {
    @Override
    public void serialize(BillHead billHead, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(billHead.getCode());
    }
}
