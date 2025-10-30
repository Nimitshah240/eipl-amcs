package com.eipl.amcs.json.deserialize;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.operation.model.BillCriteria;
import com.eipl.amcs.master.operation.repository.BillCriteriaRepository;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class BillCriteriaDeserializer extends JsonDeserializer<BillCriteria> {
    private final BillCriteriaRepository repository;

    public BillCriteriaDeserializer() {
        repository = EmcsAppContext.getContext().getBean(BillCriteriaRepository.class);
    }

    @Override
    public BillCriteria deserialize(JsonParser parser, DeserializationContext context)
            throws IOException {
        return repository.findById(String.valueOf(parser.getValueAsInt())).get();
    }

}
