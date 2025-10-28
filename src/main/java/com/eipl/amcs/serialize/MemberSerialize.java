package com.eipl.amcs.serialize;

import com.eipl.amcs.master.operation.model.Member;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class MemberSerialize extends JsonSerializer<Member> {
    @Override
    public void serialize(Member member, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(member.getCode());
    }
}
