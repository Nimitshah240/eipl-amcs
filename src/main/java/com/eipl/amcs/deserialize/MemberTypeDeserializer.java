package com.eipl.amcs.deserialize;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.global.model.MemberType;
import com.eipl.amcs.master.global.repository.MemberTypeRepository;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class MemberTypeDeserializer extends JsonDeserializer<MemberType> {
    private MemberTypeRepository repository;

    public MemberTypeDeserializer() {
        repository = EmcsAppContext.getContext().getBean(MemberTypeRepository.class);
    }

    @Override
    public MemberType deserialize(JsonParser parser, DeserializationContext context)
            throws IOException, JsonProcessingException {
        return repository.findById(parser.getValueAsInt()).get();
    }

}
