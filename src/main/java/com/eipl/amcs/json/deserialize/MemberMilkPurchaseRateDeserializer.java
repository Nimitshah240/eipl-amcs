package com.eipl.amcs.json.deserialize;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.procurement.model.MemberMilkPurchaseRate;
import com.eipl.amcs.master.procurement.repository.MemberMilkPurchaseRateRepository;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class MemberMilkPurchaseRateDeserializer extends JsonDeserializer<MemberMilkPurchaseRate> {
    private final MemberMilkPurchaseRateRepository repository;

    public MemberMilkPurchaseRateDeserializer() {
        repository = EmcsAppContext.getContext().getBean(MemberMilkPurchaseRateRepository.class);
    }

    @Override
    public MemberMilkPurchaseRate deserialize(JsonParser parser, DeserializationContext context)
            throws IOException {
        return repository.findById(String.valueOf(parser.getValueAsInt())).get();
    }

}
