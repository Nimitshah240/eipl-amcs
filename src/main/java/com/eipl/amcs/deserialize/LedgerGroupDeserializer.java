package com.eipl.amcs.deserialize;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.model.LedgerGroup;
import com.eipl.amcs.master.account.repository.LedgerGroupRepository;
import com.eipl.amcs.master.org.model.Bank;
import com.eipl.amcs.master.org.repository.BankRepository;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class LedgerGroupDeserializer extends JsonDeserializer<LedgerGroup> {
    private LedgerGroupRepository Repository;

    public LedgerGroupDeserializer() {
        Repository = EmcsAppContext.getContext().getBean(LedgerGroupRepository.class);
    }

    @Override
    public LedgerGroup deserialize(JsonParser parser, DeserializationContext context)
            throws IOException, JsonProcessingException {
        return Repository.findById(parser.getValueAsInt()).get();
    }

}
