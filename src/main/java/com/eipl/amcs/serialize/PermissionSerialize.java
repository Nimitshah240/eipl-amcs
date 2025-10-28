package com.eipl.amcs.serialize;

import com.eipl.amcs.auth.model.Permission;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class PermissionSerialize extends JsonSerializer<Permission> {
    @Override
    public void serialize(Permission permission, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeNumber(permission.getCode());
    }
}
