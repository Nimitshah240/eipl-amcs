package com.eipl.amcs.json.deserialize;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.operation.model.Vendor;
import com.eipl.amcs.master.operation.repository.VendorRepository;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class VendorDeserializer extends JsonDeserializer<Vendor> {
    private final VendorRepository repository;

    public VendorDeserializer() {
        repository = EmcsAppContext.getContext().getBean(VendorRepository.class);
    }

    @Override
    public Vendor deserialize(JsonParser parser, DeserializationContext context)
            throws IOException {
        return repository.findById(String.valueOf(parser.getValueAsInt())).get();
    }

}
