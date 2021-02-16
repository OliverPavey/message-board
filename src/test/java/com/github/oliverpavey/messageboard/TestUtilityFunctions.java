package com.github.oliverpavey.messageboard;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.oliverpavey.messageboard.SeedData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class TestUtilityFunctions {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    SeedData seedData;

    public void resetSeedData() {
        seedData.clearData();
        seedData.writeSeedData();
    }

    public String json(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.warn("Json creation failed.", e);
            throw new IllegalArgumentException("Could not convert object to JSON");
        }
    }

    public static <T> T firstElem(List<T> list) {
        if (list != null && !list.isEmpty())
            return list.get(0);
        throw new IllegalArgumentException("List is empty.");
    }

    public static <T> T lastElem(List<T> list) {
        if (list != null && !list.isEmpty())
            return list.get(list.size()-1);
        throw new IllegalArgumentException("List is empty.");
    }
}
