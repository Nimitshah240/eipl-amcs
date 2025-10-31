package com.eipl.amcs.json.deserialize;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.model.Events;
import com.eipl.amcs.master.account.repository.EventRepository;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class EventDeserializer extends JsonDeserializer<Events> {
    private final EventRepository Repository;

    public EventDeserializer() {
        Repository = EmcsAppContext.getContext().getBean(EventRepository.class);
    }

    @Override
    public Events deserialize(JsonParser parser, DeserializationContext context)
            throws IOException {
        return Repository.findById(parser.getValueAsInt()).get();
    }

}
