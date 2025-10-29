package com.eipl.amcs.deserialize;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.global.model.Shift;
import com.eipl.amcs.master.global.repository.ShiftRepository;
import com.eipl.amcs.master.org.model.Bank;
import com.eipl.amcs.master.org.repository.BankRepository;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class ShiftDeserializer extends JsonDeserializer<Shift> {
    private ShiftRepository repository;

    public ShiftDeserializer() {
        repository = EmcsAppContext.getContext().getBean(ShiftRepository.class);
    }

    @Override
    public Shift deserialize(JsonParser parser, DeserializationContext context)
            throws IOException, JsonProcessingException {
        return repository.findById(parser.getValueAsInt()).get();
    }

}
