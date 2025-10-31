package com.eipl.amcs.json.deserialize;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.org.model.Mcc;
import com.eipl.amcs.master.org.repository.MccRepository;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class MccDeserializer extends JsonDeserializer<Mcc> {
    private final MccRepository Repository;

    public MccDeserializer() {
        Repository = EmcsAppContext.getContext().getBean(MccRepository.class);
    }

    @Override
    public Mcc deserialize(JsonParser parser, DeserializationContext context)
            throws IOException {
        return Repository.findById(String.valueOf(parser.getValueAsInt())).get();
    }

}
