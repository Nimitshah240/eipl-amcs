package com.eipl.amcs.deserialize;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.org.model.Dock;
import com.eipl.amcs.master.org.repository.DockRepository;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class DockDeserializer extends JsonDeserializer<Dock> {
    private DockRepository Repository;

    public DockDeserializer() {
        Repository = EmcsAppContext.getContext().getBean(DockRepository.class);
    }

    @Override
    public Dock deserialize(JsonParser parser, DeserializationContext context)
            throws IOException, JsonProcessingException {
        return Repository.findById(String.valueOf(parser.getValueAsInt())).get();
    }

}
