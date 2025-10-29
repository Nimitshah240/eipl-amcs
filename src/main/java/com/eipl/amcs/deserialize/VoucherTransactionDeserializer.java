package com.eipl.amcs.deserialize;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.model.VoucherTransaction;
import com.eipl.amcs.master.account.repository.VoucherTransactionRepository;
import com.eipl.amcs.master.org.model.Bank;
import com.eipl.amcs.master.org.repository.BankRepository;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class VoucherTransactionDeserializer extends JsonDeserializer<VoucherTransaction> {
    private VoucherTransactionRepository repository;

    public VoucherTransactionDeserializer() {
        repository = EmcsAppContext.getContext().getBean(VoucherTransactionRepository.class);
    }

    @Override
    public VoucherTransaction deserialize(JsonParser parser, DeserializationContext context)
            throws IOException, JsonProcessingException {
        return repository.findById(String.valueOf(parser.getValueAsInt())).get();
    }

}
