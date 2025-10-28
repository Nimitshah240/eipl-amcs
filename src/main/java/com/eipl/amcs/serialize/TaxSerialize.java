package com.eipl.amcs.serialize;

import com.eipl.amcs.master.account.model.Tax;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class TaxSerialize extends JsonSerializer<Tax> {
    @Override
    public void serialize(Tax tax, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(tax.getCode());
    }
}
