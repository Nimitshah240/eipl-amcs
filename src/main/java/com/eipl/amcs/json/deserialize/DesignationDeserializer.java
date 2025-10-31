package com.eipl.amcs.json.deserialize;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.model.Designation;
import com.eipl.amcs.master.account.repository.DesignationRepository;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class DesignationDeserializer extends JsonDeserializer<Designation> {
    private final DesignationRepository repository;

    public DesignationDeserializer() {
        repository = EmcsAppContext.getContext().getBean(DesignationRepository.class);
    }

    @Override
    public Designation deserialize(JsonParser parser, DeserializationContext context)
            throws IOException {
        return repository.findById(parser.getValueAsInt()).get();
    }

}
