package com.eipl.amcs.json.deserialize;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.procurement.model.MilkReceipt;
import com.eipl.amcs.operation.procurement.repository.MilkReceiptRepository;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class MilkReceiptDeserializer extends JsonDeserializer<MilkReceipt> {
    private final MilkReceiptRepository repository;

    public MilkReceiptDeserializer() {
        repository = EmcsAppContext.getContext().getBean(MilkReceiptRepository.class);
    }

    @Override
    public MilkReceipt deserialize(JsonParser parser, DeserializationContext context)
            throws IOException {
        return repository.findById(String.valueOf(parser.getValueAsInt())).get();
    }

}
