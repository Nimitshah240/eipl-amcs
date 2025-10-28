package com.eipl.amcs.deserialize;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.geo.model.SubDistrict;
import com.eipl.amcs.master.geo.repository.SubDistrictRepository;
import com.eipl.amcs.master.org.model.Bank;
import com.eipl.amcs.master.org.repository.BankRepository;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class SubDistrictDeserializer extends JsonDeserializer<SubDistrict> {
    private SubDistrictRepository repository;

    public SubDistrictDeserializer() {
        repository = EmcsAppContext.getContext().getBean(SubDistrictRepository.class);
    }

    @Override
    public SubDistrict deserialize(JsonParser parser, DeserializationContext context)
            throws IOException, JsonProcessingException {
        return repository.findById(String.valueOf(parser.getValueAsInt())).get();
    }

}
