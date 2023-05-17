package com.springboot.kafka.average.aggregation.serdes;

import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

import java.nio.charset.StandardCharsets;
import java.util.Map;

public class JsonSerializer<T> implements Serializer<T> {

    public JsonSerializer() {
    }

    @Override
    public void configure(Map<String, ?> props, boolean isKey) {
    }
    // serialize Java Object to java Bytes String
    @Override
    public byte[] serialize(String topic, T data) {
        if (data == null)
            return null;
        try {
            //  JsonMapper.writeToJson(data) write java object to java String and then getBytes convert string to
            //  java bytes (UTF_8) stream
            return JsonMapper.writeToJson(data).getBytes(StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new SerializationException("Error serializing JSON message", e);
        }
    }

    @Override
    public void close() {
    }
}
