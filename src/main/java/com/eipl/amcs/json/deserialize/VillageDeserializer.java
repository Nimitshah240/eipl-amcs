package com.eipl.amcs.json.deserialize;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.geo.model.Village;
import com.eipl.amcs.master.geo.repository.VillageRepository;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class VillageDeserializer extends JsonDeserializer<Village> {
    private final VillageRepository repository;

    public VillageDeserializer() {
        repository = EmcsAppContext.getContext().getBean(VillageRepository.class);
    }

    @Override
    public Village deserialize(JsonParser parser, DeserializationContext context)
            throws IOException {
        return repository.findById(String.valueOf(parser.getValueAsInt())).get();
    }

}
