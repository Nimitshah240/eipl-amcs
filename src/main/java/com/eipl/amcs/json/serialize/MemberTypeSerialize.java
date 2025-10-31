package com.eipl.amcs.json.serialize;

import com.eipl.amcs.master.global.model.MemberType;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class MemberTypeSerialize extends JsonSerializer<MemberType> {
    @Override
    public void serialize(MemberType memberType, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeNumber(memberType.getCode());
    }
}
