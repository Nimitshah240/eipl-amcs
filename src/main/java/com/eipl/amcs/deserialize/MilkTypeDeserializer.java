package com.eipl.amcs.deserialize;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.global.repository.MilkTypeRepository;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class MilkTypeDeserializer extends JsonDeserializer<MilkType> {
    private MilkTypeRepository repository;

    public MilkTypeDeserializer() {
        repository = EmcsAppContext.getContext().getBean(MilkTypeRepository.class);
    }

    @Override
    public MilkType deserialize(JsonParser parser, DeserializationContext context)
            throws IOException, JsonProcessingException {
        return repository.findByCode(parser.getValueAsInt());
    }

}
