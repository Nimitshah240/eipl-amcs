package com.eipl.amcs.json.serialize;

import com.eipl.amcs.master.account.model.Events;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class EventSerialize extends JsonSerializer<Events> {
    @Override
    public void serialize(Events events, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeNumber(events.getCode());
    }
}
