package com.eipl.amcs.serialize;

import com.eipl.amcs.operation.procurement.model.MilkDispatch;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class MilkDispatchSerialize extends JsonSerializer<MilkDispatch> {
    @Override
    public void serialize(MilkDispatch milkDispatch, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(milkDispatch.getChallanNo());
    }
}
