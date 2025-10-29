package com.eipl.amcs.json.deserialize;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.model.Mom;
import com.eipl.amcs.master.account.repository.MomRepository;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class MomDeserializer extends JsonDeserializer<Mom> {
    private MomRepository repository;

    public MomDeserializer() {
        repository = EmcsAppContext.getContext().getBean(MomRepository.class);
    }

    @Override
    public Mom deserialize(JsonParser parser, DeserializationContext context)
            throws IOException, JsonProcessingException {
        return repository.findById(String.valueOf(parser.getValueAsInt())).get();
    }

}
