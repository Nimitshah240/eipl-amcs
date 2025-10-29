package com.eipl.amcs.json.deserialize;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.operation.model.BillHead;
import com.eipl.amcs.master.operation.repository.BillHeadRepository;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class BillHeadDeserializer extends JsonDeserializer<BillHead> {
    private BillHeadRepository repository;

    public BillHeadDeserializer() {
        repository = EmcsAppContext.getContext().getBean(BillHeadRepository.class);
    }

    @Override
    public BillHead deserialize(JsonParser parser, DeserializationContext context)
            throws IOException, JsonProcessingException {
        return repository.findById(String.valueOf(parser.getValueAsInt())).get();
    }

}
