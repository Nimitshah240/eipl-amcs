package com.eipl.amcs.json.deserialize;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.procurement.model.MilkDispatch;
import com.eipl.amcs.operation.procurement.repository.MilkDispatchRepository;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class MilkDispatchDeserializer extends JsonDeserializer<MilkDispatch> {
    private final MilkDispatchRepository repository;

    public MilkDispatchDeserializer() {
        repository = EmcsAppContext.getContext().getBean(MilkDispatchRepository.class);
    }

    @Override
    public MilkDispatch deserialize(JsonParser parser, DeserializationContext context)
            throws IOException {
        return repository.findById(String.valueOf(parser.getValueAsInt())).get();
    }

}
