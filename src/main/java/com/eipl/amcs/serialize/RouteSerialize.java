package com.eipl.amcs.serialize;

import com.eipl.amcs.master.org.model.Route;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class RouteSerialize extends JsonSerializer<Route> {
    @Override
    public void serialize(Route route, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(route.getCode());
    }
}
