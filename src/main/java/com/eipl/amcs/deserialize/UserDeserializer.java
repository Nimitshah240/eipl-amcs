package com.eipl.amcs.deserialize;

import com.eipl.amcs.auth.model.User;
import com.eipl.amcs.auth.repository.UserRepository;
import com.eipl.amcs.config.EmcsAppContext;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class UserDeserializer extends JsonDeserializer<User> {
    private UserRepository repository;

    public UserDeserializer() {
        repository = EmcsAppContext.getContext().getBean(UserRepository.class);
    }

    @Override
    public User deserialize(JsonParser parser, DeserializationContext context)
            throws IOException, JsonProcessingException {
        return repository.findById(String.valueOf(parser.getValueAsInt())).get();
    }

}
