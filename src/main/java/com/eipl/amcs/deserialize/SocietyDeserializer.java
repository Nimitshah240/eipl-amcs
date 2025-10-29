package com.eipl.amcs.deserialize;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.org.repository.SocietyRepository;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class SocietyDeserializer extends JsonDeserializer<Society> {
    private SocietyRepository societyRepository;

    public SocietyDeserializer() {
        societyRepository = EmcsAppContext.getContext().getBean(SocietyRepository.class);
    }

    @Override
    public Society deserialize(JsonParser parser, DeserializationContext context)
            throws IOException, JsonProcessingException {
        return societyRepository.findById(String.valueOf(parser.getValueAsInt())).get();
    }

}
