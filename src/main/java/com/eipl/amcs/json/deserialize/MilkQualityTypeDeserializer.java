package com.eipl.amcs.json.deserialize;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.global.model.MilkQualityType;
import com.eipl.amcs.master.global.repository.MilkQualityTypeRepository;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class MilkQualityTypeDeserializer extends JsonDeserializer<MilkQualityType> {
    private final MilkQualityTypeRepository repository;

    public MilkQualityTypeDeserializer() {
        repository = EmcsAppContext.getContext().getBean(MilkQualityTypeRepository.class);
    }

    @Override
    public MilkQualityType deserialize(JsonParser parser, DeserializationContext context)
            throws IOException {
        return repository.findById(parser.getValueAsInt()).get();
    }

}
