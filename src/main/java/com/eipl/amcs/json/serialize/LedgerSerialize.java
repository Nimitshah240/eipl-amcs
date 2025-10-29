package com.eipl.amcs.json.serialize;

import com.eipl.amcs.master.account.model.Ledger;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class LedgerSerialize extends JsonSerializer<Ledger> {
    @Override
    public void serialize(Ledger ledger, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(ledger.getCode());
    }
}
