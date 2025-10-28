package com.eipl.amcs.serialize;

import com.eipl.amcs.master.geo.model.State;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class StateSerialize extends JsonSerializer<State> {
    @Override
    public void serialize(State state, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(state.getCode());
    }
}
