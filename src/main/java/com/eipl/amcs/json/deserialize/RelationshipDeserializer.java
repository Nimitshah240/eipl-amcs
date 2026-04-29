package com.eipl.amcs.json.deserialize;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.operation.model.Relationship;
import com.eipl.amcs.master.operation.repository.RelationshipRepository;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class RelationshipDeserializer extends JsonDeserializer<Relationship> {
    private final RelationshipRepository Repository;

    public RelationshipDeserializer() {
        Repository = EmcsAppContext.getContext().getBean(RelationshipRepository.class);
    }

    @Override
    public Relationship deserialize(JsonParser parser, DeserializationContext context)
            throws IOException {
        return Repository.findById(parser.getValueAsInt()).get();
    }

}
