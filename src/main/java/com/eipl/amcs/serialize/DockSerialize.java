package com.eipl.amcs.serialize;

import com.eipl.amcs.master.org.model.Dock;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class DockSerialize extends JsonSerializer<Dock> {
    @Override
    public void serialize(Dock dock, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(dock.getDockNo());
    }
}
