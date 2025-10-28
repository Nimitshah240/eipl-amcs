package com.eipl.amcs.serialize;

import com.eipl.amcs.master.org.model.Branch;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class BranchSerialize extends JsonSerializer<Branch> {
    @Override
    public void serialize(Branch branch, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(branch.getCode());
    }
}
