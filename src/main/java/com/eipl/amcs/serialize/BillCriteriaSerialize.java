package com.eipl.amcs.serialize;

import com.eipl.amcs.master.operation.model.BillCriteria;
import com.eipl.amcs.master.org.model.Bank;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class BillCriteriaSerialize extends JsonSerializer<BillCriteria> {
    @Override
    public void serialize(BillCriteria billCriteria, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(billCriteria.getCode());
    }
}
