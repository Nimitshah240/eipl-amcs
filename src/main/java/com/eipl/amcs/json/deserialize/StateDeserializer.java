package com.eipl.amcs.json.deserialize;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.geo.model.State;
import com.eipl.amcs.master.geo.repository.StateRepository;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class StateDeserializer extends JsonDeserializer<State> {
    private final StateRepository repository;

    public StateDeserializer() {
        repository = EmcsAppContext.getContext().getBean(StateRepository.class);
    }

    @Override
    public State deserialize(JsonParser parser, DeserializationContext context)
            throws IOException {
        return repository.findById(String.valueOf(parser.getValueAsInt())).get();
    }

}
