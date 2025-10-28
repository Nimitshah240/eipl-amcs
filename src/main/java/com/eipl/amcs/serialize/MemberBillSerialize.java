package com.eipl.amcs.serialize;

import com.eipl.amcs.operation.billing.model.MemberBill;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class MemberBillSerialize extends JsonSerializer<MemberBill> {
    @Override
    public void serialize(MemberBill memberBill, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(memberBill.getCode());
    }
}
