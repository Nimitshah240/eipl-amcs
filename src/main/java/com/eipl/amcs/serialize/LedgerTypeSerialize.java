package com.eipl.amcs.serialize;

import com.eipl.amcs.master.account.model.LedgerType;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class LedgerTypeSerialize extends JsonSerializer<LedgerType> {
    @Override
    public void serialize(LedgerType ledgerType, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeNumber(ledgerType.getCode());
    }
}
