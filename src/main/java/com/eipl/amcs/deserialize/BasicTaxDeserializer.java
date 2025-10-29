package com.eipl.amcs.deserialize;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.model.BasicTax;
import com.eipl.amcs.master.account.repository.BasicTaxRepository;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class BasicTaxDeserializer extends JsonDeserializer<BasicTax> {
    private BasicTaxRepository repository;

    public BasicTaxDeserializer() {
        repository = EmcsAppContext.getContext().getBean(BasicTaxRepository.class);
    }

    @Override
    public BasicTax deserialize(JsonParser parser, DeserializationContext context)
            throws IOException, JsonProcessingException {
        return repository.findById(parser.getValueAsInt()).get();
    }

}
