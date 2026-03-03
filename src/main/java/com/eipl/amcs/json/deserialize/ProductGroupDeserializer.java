package com.eipl.amcs.json.deserialize;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.inventory.model.ProductGroup;
import com.eipl.amcs.master.inventory.repository.ProductGroupRepository;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class ProductGroupDeserializer extends JsonDeserializer<ProductGroup> {
    private final ProductGroupRepository productGroupRepository;

    public ProductGroupDeserializer() {
        productGroupRepository = EmcsAppContext.getContext().getBean(ProductGroupRepository.class);
    }

    @Override
    public ProductGroup deserialize(JsonParser parser, DeserializationContext context)
            throws IOException {
        return productGroupRepository.findById(parser.getValueAsInt()).get();
    }

}
