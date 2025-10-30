package com.eipl.amcs.json.deserialize;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.procurement.model.SocietyMilkPurchaseRate;
import com.eipl.amcs.master.procurement.repository.SocietyMilkPurchaseRateRepository;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class SocietyMilkPurchaseRateDeserializer extends JsonDeserializer<SocietyMilkPurchaseRate> {
    private final SocietyMilkPurchaseRateRepository repository;

    public SocietyMilkPurchaseRateDeserializer() {
        repository = EmcsAppContext.getContext().getBean(SocietyMilkPurchaseRateRepository.class);
    }

    @Override
    public SocietyMilkPurchaseRate deserialize(JsonParser parser, DeserializationContext context)
            throws IOException {
        return repository.findById(String.valueOf(parser.getValueAsInt())).get();
    }

}
