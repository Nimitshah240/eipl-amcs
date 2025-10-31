package com.eipl.amcs.json.deserialize;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.global.model.RateType;
import com.eipl.amcs.master.global.repository.RateTypeRepository;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class RateTypeDeserializer extends JsonDeserializer<RateType> {
    private final RateTypeRepository repository;

    public RateTypeDeserializer() {
        repository = EmcsAppContext.getContext().getBean(RateTypeRepository.class);
    }

    @Override
    public RateType deserialize(JsonParser parser, DeserializationContext context)
            throws IOException {
        return repository.findById(parser.getValueAsInt()).get();
    }

}
