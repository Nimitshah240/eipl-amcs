package com.eipl.amcs.serialize;

import com.eipl.amcs.master.org.model.Bank;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class BankSerialize extends JsonSerializer<Bank> {
    @Override
    public void serialize(Bank bank, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(bank.getCode());
    }
}
