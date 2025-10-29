package com.eipl.amcs.json.deserialize;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.org.model.Branch;
import com.eipl.amcs.master.org.repository.BranchRepository;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class BranchDeserializer extends JsonDeserializer<Branch> {
    private BranchRepository repository;

    public BranchDeserializer() {
        repository = EmcsAppContext.getContext().getBean(BranchRepository.class);
    }

    @Override
    public Branch deserialize(JsonParser parser, DeserializationContext context)
            throws IOException, JsonProcessingException {
        return repository.findById(String.valueOf(parser.getValueAsInt())).get();
    }

}
