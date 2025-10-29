package com.eipl.amcs.deserialize;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.org.model.Bank;
import com.eipl.amcs.master.org.repository.BankRepository;
import com.eipl.amcs.operation.inventory.model.ProductSaleTransaction;
import com.eipl.amcs.operation.inventory.repository.ProductSaleTransactionRepository;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class ProductSaleTransactionDeserializer extends JsonDeserializer<ProductSaleTransaction> {
    private ProductSaleTransactionRepository repository;

    public ProductSaleTransactionDeserializer() {
        repository = EmcsAppContext.getContext().getBean(ProductSaleTransactionRepository.class);
    }

    @Override
    public ProductSaleTransaction deserialize(JsonParser parser, DeserializationContext context)
            throws IOException, JsonProcessingException {
        return repository.findById(String.valueOf(parser.getValueAsInt())).get();
    }

}
