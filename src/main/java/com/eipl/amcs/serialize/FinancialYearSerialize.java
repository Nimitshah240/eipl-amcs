package com.eipl.amcs.serialize;

import com.eipl.amcs.master.account.model.FinancialYear;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class FinancialYearSerialize extends JsonSerializer<FinancialYear> {
    @Override
    public void serialize(FinancialYear financialYear, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(financialYear.getCode());
    }
}
