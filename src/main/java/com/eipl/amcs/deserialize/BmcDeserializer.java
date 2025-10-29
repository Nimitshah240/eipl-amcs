package com.eipl.amcs.deserialize;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.org.model.Bmc;
import com.eipl.amcs.master.org.repository.BmcRepository;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class BmcDeserializer extends JsonDeserializer<Bmc> {
    private BmcRepository repository;

    public BmcDeserializer() {
        repository = EmcsAppContext.getContext().getBean(BmcRepository.class);
    }

    @Override
    public Bmc deserialize(JsonParser parser, DeserializationContext context)
            throws IOException, JsonProcessingException {
        return repository.findById(String.valueOf(parser.getValueAsInt())).get();
    }

}
