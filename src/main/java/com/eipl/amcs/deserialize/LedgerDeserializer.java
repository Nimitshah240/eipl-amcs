package com.eipl.amcs.deserialize;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.model.Ledger;
import com.eipl.amcs.master.account.repository.LedgerRepository;
import com.eipl.amcs.master.org.model.Bank;
import com.eipl.amcs.master.org.repository.BankRepository;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class LedgerDeserializer extends JsonDeserializer<Ledger> {
    private LedgerRepository Repository;

    public LedgerDeserializer() {
        Repository = EmcsAppContext.getContext().getBean(LedgerRepository.class);
    }

    @Override
    public Ledger deserialize(JsonParser parser, DeserializationContext context)
            throws IOException, JsonProcessingException {
        return Repository.findById(String.valueOf(parser.getValueAsInt())).get();
    }

}
