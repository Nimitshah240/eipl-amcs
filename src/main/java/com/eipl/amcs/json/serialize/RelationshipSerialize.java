package com.eipl.amcs.json.serialize;

import com.eipl.amcs.master.operation.model.Relationship;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class RelationshipSerialize extends JsonSerializer<Relationship> {
    @Override
    public void serialize(Relationship relationship, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeNumber(relationship.getRelationshipCode());
    }
}
