package com.rudolfs.examples.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public final class ObjectMapperFactory {

    private static final ObjectMapper MAPPER = new ObjectMapper()
            .registerModules(new JavaTimeModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

    public static ObjectMapper objectMapper() {
        return MAPPER;
    }
}
