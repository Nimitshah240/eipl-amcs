package com.eipl.amcs.deserialize;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.geo.model.District;
import com.eipl.amcs.master.geo.repository.DistrictRepository;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class DistrictDeserializer extends JsonDeserializer<District> {
    private DistrictRepository repository;

    public DistrictDeserializer() {
        repository = EmcsAppContext.getContext().getBean(DistrictRepository.class);
    }

    @Override
    public District deserialize(JsonParser parser, DeserializationContext context)
            throws IOException, JsonProcessingException {
        return repository.findById(String.valueOf(parser.getValueAsInt())).get();
    }

}
