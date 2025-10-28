package com.eipl.amcs.deserialize;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.global.model.MilkClass;
import com.eipl.amcs.master.global.repository.MilkClassRepository;
import com.eipl.amcs.master.org.model.Bank;
import com.eipl.amcs.master.org.repository.BankRepository;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class MilkClassDeserializer extends JsonDeserializer<MilkClass> {
    private MilkClassRepository repository;

    public MilkClassDeserializer() {
        repository = EmcsAppContext.getContext().getBean(MilkClassRepository.class);
    }

    @Override
    public MilkClass deserialize(JsonParser parser, DeserializationContext context)
            throws IOException, JsonProcessingException {
        return repository.findById(parser.getValueAsInt()).get();
    }

}
