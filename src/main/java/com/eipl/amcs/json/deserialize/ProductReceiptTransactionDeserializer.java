package com.eipl.amcs.json.deserialize;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.inventory.model.ProductReceiptTransaction;
import com.eipl.amcs.operation.inventory.repository.ProductReceiptTransactionRepository;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class ProductReceiptTransactionDeserializer extends JsonDeserializer<ProductReceiptTransaction> {
    private final ProductReceiptTransactionRepository repository;

    public ProductReceiptTransactionDeserializer() {
        repository = EmcsAppContext.getContext().getBean(ProductReceiptTransactionRepository.class);
    }

    @Override
    public ProductReceiptTransaction deserialize(JsonParser parser, DeserializationContext context)
            throws IOException {
        return repository.findById(String.valueOf(parser.getValueAsInt())).get();
    }

}
