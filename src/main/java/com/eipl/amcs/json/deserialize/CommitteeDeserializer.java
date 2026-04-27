package com.eipl.amcs.json.deserialize;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.model.Committee;
import com.eipl.amcs.master.account.repository.CommitteeRepository;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class CommitteeDeserializer extends JsonDeserializer<Committee> {
    private final CommitteeRepository repository;

    public CommitteeDeserializer() {
        repository = EmcsAppContext.getContext().getBean(CommitteeRepository.class);
    }

    @Override
    public Committee deserialize(JsonParser parser, DeserializationContext context)
            throws IOException {
        return repository.findById(parser.getValueAsString()).get();
    }
}