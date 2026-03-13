package com.eipl.amcs.json.deserialize;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.model.VoucherType;
import com.eipl.amcs.master.account.repository.VoucherTypeRepository;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class VoucherTypeDeserializer extends JsonDeserializer<VoucherType> {
    private final VoucherTypeRepository repository;

    public VoucherTypeDeserializer() {
        repository = EmcsAppContext.getContext().getBean(VoucherTypeRepository.class);
    }

    @Override
    public VoucherType deserialize(JsonParser parser, DeserializationContext context)
            throws IOException {
        return repository.findById(Long.valueOf(parser.getValueAsString())).get();
    }

}
