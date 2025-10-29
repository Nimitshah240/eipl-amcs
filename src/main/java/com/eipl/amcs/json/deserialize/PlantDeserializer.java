package com.eipl.amcs.json.deserialize;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.org.model.Plant;
import com.eipl.amcs.master.org.repository.PlantRepository;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class PlantDeserializer extends JsonDeserializer<Plant> {
    private PlantRepository repository;

    public PlantDeserializer() {
        repository = EmcsAppContext.getContext().getBean(PlantRepository.class);
    }

    @Override
    public Plant deserialize(JsonParser parser, DeserializationContext context)
            throws IOException, JsonProcessingException {
        return repository.findById(String.valueOf(parser.getValueAsInt())).get();
    }

}
