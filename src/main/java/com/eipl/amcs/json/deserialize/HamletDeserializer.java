package com.eipl.amcs.json.deserialize;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.geo.model.Hamlet;
import com.eipl.amcs.master.geo.repository.HamletRepository;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class HamletDeserializer extends JsonDeserializer<Hamlet> {
    private HamletRepository Repository;

    public HamletDeserializer() {
        Repository = EmcsAppContext.getContext().getBean(HamletRepository.class);
    }

    @Override
    public Hamlet deserialize(JsonParser parser, DeserializationContext context)
            throws IOException, JsonProcessingException {
        return Repository.findById(String.valueOf(parser.getValueAsInt())).get();
    }

}
