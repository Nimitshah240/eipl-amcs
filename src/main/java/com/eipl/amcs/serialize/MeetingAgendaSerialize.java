package com.eipl.amcs.serialize;

import com.eipl.amcs.master.account.model.MeetingAgenda;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class MeetingAgendaSerialize extends JsonSerializer<MeetingAgenda> {
    @Override
    public void serialize(MeetingAgenda meetingAgenda, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(meetingAgenda.getCode());
    }
}
