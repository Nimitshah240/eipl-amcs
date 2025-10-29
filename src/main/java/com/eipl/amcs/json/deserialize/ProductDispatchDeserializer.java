package com.eipl.amcs.json.deserialize;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.inventory.model.ProductDispatch;
import com.eipl.amcs.operation.inventory.repository.ProductDispatchRepository;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class ProductDispatchDeserializer extends JsonDeserializer<ProductDispatch> {
    private ProductDispatchRepository repository;

    public ProductDispatchDeserializer() {
        repository = EmcsAppContext.getContext().getBean(ProductDispatchRepository.class);
    }

    @Override
    public ProductDispatch deserialize(JsonParser parser, DeserializationContext context)
            throws IOException, JsonProcessingException {
        return repository.findById(String.valueOf(parser.getValueAsInt())).get();
    }

}
