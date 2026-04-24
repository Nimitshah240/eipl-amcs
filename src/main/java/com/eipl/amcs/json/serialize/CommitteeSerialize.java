package com.eipl.amcs.json.serialize;

import com.eipl.amcs.master.account.model.Committee;
import com.eipl.amcs.master.account.model.Designation;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class CommitteeSerialize extends JsonSerializer<Committee> {
    @Override
    public void serialize(Committee designation, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeNumber(designation.getCode());
    }
}
