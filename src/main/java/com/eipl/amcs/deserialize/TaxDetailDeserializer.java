package com.eipl.amcs.deserialize;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.model.TaxDetail;
import com.eipl.amcs.master.account.repository.TaxDetailRepository;
import com.eipl.amcs.master.org.model.Bank;
import com.eipl.amcs.master.org.repository.BankRepository;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class TaxDetailDeserializer extends JsonDeserializer<TaxDetail> {
    private TaxDetailRepository repository;

    public TaxDetailDeserializer() {
        repository = EmcsAppContext.getContext().getBean(TaxDetailRepository.class);
    }

    @Override
    public TaxDetail deserialize(JsonParser parser, DeserializationContext context)
            throws IOException, JsonProcessingException {
        return repository.findById(String.valueOf(parser.getValueAsInt())).get();
    }

}
