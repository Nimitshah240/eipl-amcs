package com.eipl.amcs.json.deserialize;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.model.MeetingAgenda;
import com.eipl.amcs.master.account.repository.MeetingAgendaRepository;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class MeetingAgendaDeserializer extends JsonDeserializer<MeetingAgenda> {
    private MeetingAgendaRepository repository;

    public MeetingAgendaDeserializer() {
        repository = EmcsAppContext.getContext().getBean(MeetingAgendaRepository.class);
    }

    @Override
    public MeetingAgenda deserialize(JsonParser parser, DeserializationContext context)
            throws IOException, JsonProcessingException {
        return repository.findById(String.valueOf(parser.getValueAsInt())).get();
    }

}
