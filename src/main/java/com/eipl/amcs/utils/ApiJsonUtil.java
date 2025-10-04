package com.eipl.amcs.utils;


import com.eipl.amcs.exception.apierror.ApiError;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ApiJsonUtil {
    private final ObjectMapper mapper;

    public ApiJsonUtil() {
        mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
    }

    public String getJsonString(ApiError obj) {
        String jsonString = "";
        try {
            jsonString = mapper.writeValueAsString(obj);
        } catch (Exception e) {
        }
        return jsonString;
    }

    public ApiError parseJsonString(String jsonText) throws JsonParseException {
        try {
            return mapper.readValue(jsonText, ApiError.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
