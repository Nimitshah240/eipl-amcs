package com.eipl.amcs.serialize;

import com.eipl.amcs.master.account.model.LedgerGroup;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class LedgerGroupSerialize extends JsonSerializer<LedgerGroup> {
    @Override
    public void serialize(LedgerGroup ledgerGroup, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeNumber(ledgerGroup.getCode());
    }
}
