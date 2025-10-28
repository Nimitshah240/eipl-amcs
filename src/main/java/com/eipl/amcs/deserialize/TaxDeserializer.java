package com.eipl.amcs.deserialize;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.model.Tax;
import com.eipl.amcs.master.account.repository.TaxRepository;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class TaxDeserializer extends JsonDeserializer<Tax> {
    private TaxRepository taxRepository;

    public TaxDeserializer() {
        taxRepository = EmcsAppContext.getContext().getBean(TaxRepository.class);
    }

    @Override
    public Tax deserialize(JsonParser parser, DeserializationContext context)
            throws IOException, JsonProcessingException {
        return taxRepository.findById(String.valueOf(parser.getValueAsInt())).get();
    }

}
