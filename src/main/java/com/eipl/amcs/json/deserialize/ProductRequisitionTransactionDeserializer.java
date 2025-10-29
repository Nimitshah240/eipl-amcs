package com.eipl.amcs.json.deserialize;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.inventory.model.ProductRequisitionTransaction;
import com.eipl.amcs.operation.inventory.repository.ProductRequisitionTransactionRepository;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class ProductRequisitionTransactionDeserializer extends JsonDeserializer<ProductRequisitionTransaction> {
    private ProductRequisitionTransactionRepository repository;

    public ProductRequisitionTransactionDeserializer() {
        repository = EmcsAppContext.getContext().getBean(ProductRequisitionTransactionRepository.class);
    }

    @Override
    public ProductRequisitionTransaction deserialize(JsonParser parser, DeserializationContext context)
            throws IOException, JsonProcessingException {
        return repository.findById(String.valueOf(parser.getValueAsInt())).get();
    }

}
