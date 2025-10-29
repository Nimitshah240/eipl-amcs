package com.eipl.amcs.serialize;

import com.eipl.amcs.master.org.model.Plant;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class PlantSerialize extends JsonSerializer<Plant> {
    @Override
    public void serialize(Plant plant, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(plant.getCode());
    }
}
