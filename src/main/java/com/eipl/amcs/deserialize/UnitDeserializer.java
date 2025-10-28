package com.eipl.amcs.deserialize;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.global.model.Unit;
import com.eipl.amcs.master.global.repository.UnitRepository;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class UnitDeserializer extends JsonDeserializer<Unit> {
    private UnitRepository unitRepository;

    public UnitDeserializer() {
        unitRepository = EmcsAppContext.getContext().getBean(UnitRepository.class);
    }

    @Override
    public Unit deserialize(JsonParser parser, DeserializationContext context)
            throws IOException, JsonProcessingException {
        return unitRepository.findById(parser.getValueAsInt()).get();
    }

}
