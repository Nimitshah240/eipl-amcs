package com.eipl.amcs.json.deserialize;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.global.model.Gender;
import com.eipl.amcs.master.global.repository.GenderRepository;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class GenderDeserializer extends JsonDeserializer<Gender> {
    private final GenderRepository Repository;

    public GenderDeserializer() {
        Repository = EmcsAppContext.getContext().getBean(GenderRepository.class);
    }

    @Override
    public Gender deserialize(JsonParser parser, DeserializationContext context)
            throws IOException {
        return Repository.findById(parser.getValueAsInt()).get();
    }

}
