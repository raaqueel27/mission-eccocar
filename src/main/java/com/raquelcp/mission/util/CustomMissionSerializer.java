package com.raquelcp.mission.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.raquelcp.mission.persistence.entity.Mission;

public class CustomMissionSerializer extends StdSerializer<List<Mission>>{
    public CustomMissionSerializer() {
        this(null);
    }

    public CustomMissionSerializer(Class<List<Mission>> t) {
        super(t);
    }

    @Override
    public void serialize(
            List<Mission> missions,
            JsonGenerator generator,
            SerializerProvider provider)
            throws IOException, JsonProcessingException {

        List<Mission> ms = new ArrayList<>();
        for (Mission m : missions) {
            m.setCaptains(null);
            m.setPlanets(null);
            ms.add(m);
        }
        generator.writeObject(ms);
    }
}
