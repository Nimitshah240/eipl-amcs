package com.eipl.amcs.deserialize;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.operation.model.Formula;
import com.eipl.amcs.master.operation.repository.FormulaRepository;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class FormulaDeserializer extends JsonDeserializer<Formula> {
    private FormulaRepository Repository;

    public FormulaDeserializer() {
        Repository = EmcsAppContext.getContext().getBean(FormulaRepository.class);
    }

    @Override
    public Formula deserialize(JsonParser parser, DeserializationContext context)
            throws IOException, JsonProcessingException {
        return Repository.findById(String.valueOf(parser.getValueAsInt())).get();
    }

}
