package com.springboot.kafka.average.aggregation.serdes;



import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

import java.nio.charset.StandardCharsets;
import java.util.Map;

public class JsonDeserializer<T> implements Deserializer<T> {

    private Class<T> destinationClass;

    public JsonDeserializer(Class<T> destinationClass) {
        this.destinationClass = destinationClass;
    }

    @Override
    public void configure(Map<String, ?> props, boolean isKey) {
    }
    // Deserialize byte[] to an objects
    @Override
    public T deserialize(String topic, byte[] bytes) {
        if (bytes == null)
            return null;
        try {
            // convert bytes to UTF_8 String and then JsonMapper.readFromJson convert string to Java Object
            return JsonMapper.readFromJson(new String(bytes, StandardCharsets.UTF_8), destinationClass);
        } catch (Exception e) {
            throw new SerializationException("Error deserializing message", e);
        }
    }

    @Override
    public void close() {
    }
}
