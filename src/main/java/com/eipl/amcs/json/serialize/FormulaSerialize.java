package com.eipl.amcs.json.serialize;

import com.eipl.amcs.master.operation.model.Formula;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class FormulaSerialize extends JsonSerializer<Formula> {
    @Override
    public void serialize(Formula formula, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(formula.getCode());
    }
}
