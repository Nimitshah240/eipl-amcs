package com.eipl.amcs.json.deserialize;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.procurement.model.SocietyPaymentCycle;
import com.eipl.amcs.master.procurement.repository.SocietyPaymentCycleRepository;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class SocietyPaymentCycleDeserializer extends JsonDeserializer<SocietyPaymentCycle> {
    private final SocietyPaymentCycleRepository repository;

    public SocietyPaymentCycleDeserializer() {
        repository = EmcsAppContext.getContext().getBean(SocietyPaymentCycleRepository.class);
    }

    @Override
    public SocietyPaymentCycle deserialize(JsonParser parser, DeserializationContext context)
            throws IOException {
        return repository.findById(String.valueOf(parser.getValueAsInt())).get();
    }

}
