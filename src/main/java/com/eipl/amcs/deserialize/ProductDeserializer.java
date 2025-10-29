package com.eipl.amcs.deserialize;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.inventory.model.Product;
import com.eipl.amcs.master.inventory.repository.ProductRepository;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class ProductDeserializer extends JsonDeserializer<Product> {
    private ProductRepository productRepository;

    public ProductDeserializer() {
        productRepository = EmcsAppContext.getContext().getBean(ProductRepository.class);
    }

    @Override
    public Product deserialize(JsonParser parser, DeserializationContext context)
            throws IOException, JsonProcessingException {
        return productRepository.findById(String.valueOf(parser.getValueAsInt())).get();
    }

}
