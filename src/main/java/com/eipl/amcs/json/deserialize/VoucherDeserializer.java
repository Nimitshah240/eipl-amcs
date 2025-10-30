package com.eipl.amcs.json.deserialize;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.model.Voucher;
import com.eipl.amcs.master.account.repository.VoucherRepository;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class VoucherDeserializer extends JsonDeserializer<Voucher> {
    private final VoucherRepository repository;

    public VoucherDeserializer() {
        repository = EmcsAppContext.getContext().getBean(VoucherRepository.class);
    }

    @Override
    public Voucher deserialize(JsonParser parser, DeserializationContext context)
            throws IOException {
        return repository.findById(String.valueOf(parser.getValueAsInt())).get();
    }

}
