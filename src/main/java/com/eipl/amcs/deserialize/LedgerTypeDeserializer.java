package com.eipl.amcs.deserialize;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.model.LedgerType;
import com.eipl.amcs.master.account.repository.LedgerTypeRepository;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class LedgerTypeDeserializer extends JsonDeserializer<LedgerType> {
    private LedgerTypeRepository Repository;

    public LedgerTypeDeserializer() {
        Repository = EmcsAppContext.getContext().getBean(LedgerTypeRepository.class);
    }

    @Override
    public LedgerType deserialize(JsonParser parser, DeserializationContext context)
            throws IOException, JsonProcessingException {
        return Repository.findById(parser.getValueAsInt()).get();
    }

}
