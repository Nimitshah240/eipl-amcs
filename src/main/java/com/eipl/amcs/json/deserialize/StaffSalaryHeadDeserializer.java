package com.eipl.amcs.json.deserialize;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.model.StaffMember;
import com.eipl.amcs.master.account.repository.StaffMemberRepository;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class StaffSalaryHeadDeserializer extends JsonDeserializer<StaffMember> {
    private StaffMemberRepository repository;

    public StaffSalaryHeadDeserializer() {
        repository = EmcsAppContext.getContext().getBean(StaffMemberRepository.class);
    }

    @Override
    public StaffMember deserialize(JsonParser parser, DeserializationContext context)
            throws IOException, JsonProcessingException {
        return repository.findById(String.valueOf(parser.getValueAsInt())).get();
    }

}
