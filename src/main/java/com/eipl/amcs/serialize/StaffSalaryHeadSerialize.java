package com.eipl.amcs.serialize;

import com.eipl.amcs.master.account.model.StaffSalaryHead;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class StaffSalaryHeadSerialize extends JsonSerializer<StaffSalaryHead> {
    @Override
    public void serialize(StaffSalaryHead staffSalaryHead, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeNumber(staffSalaryHead.getCode());
    }
}
