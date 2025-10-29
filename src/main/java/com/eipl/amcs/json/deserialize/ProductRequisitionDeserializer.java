package com.eipl.amcs.json.deserialize;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.inventory.model.ProductRequisition;
import com.eipl.amcs.operation.inventory.repository.ProductRequisitionRepository;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class ProductRequisitionDeserializer extends JsonDeserializer<ProductRequisition> {
    private ProductRequisitionRepository repository;

    public ProductRequisitionDeserializer() {
        repository = EmcsAppContext.getContext().getBean(ProductRequisitionRepository.class);
    }

    @Override
    public ProductRequisition deserialize(JsonParser parser, DeserializationContext context)
            throws IOException, JsonProcessingException {
        return repository.findById(String.valueOf(parser.getValueAsInt())).get();
    }

}
