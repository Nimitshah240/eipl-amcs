package com.eipl.amcs.deserialize;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.org.model.Union;
import com.eipl.amcs.master.org.repository.UnionRepository;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class UnionDeserializer extends JsonDeserializer<Union> {
    private UnionRepository unionRepository;

    public UnionDeserializer() {
        unionRepository = EmcsAppContext.getContext().getBean(UnionRepository.class);
    }

    @Override
    public Union deserialize(JsonParser parser, DeserializationContext context)
            throws IOException, JsonProcessingException {
        return unionRepository.findById(String.valueOf(parser.getValueAsInt())).get();
    }

}
