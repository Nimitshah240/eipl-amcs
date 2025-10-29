package com.eipl.amcs.serialize;

import com.eipl.amcs.master.account.model.SubLedger;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class SubLedgerSerialize extends JsonSerializer<SubLedger> {
    @Override
    public void serialize(SubLedger subLedger, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(subLedger.getCode());
    }
}
