package com.springboot.kafka.average.aggregation.serdeImpl;

import com.springboot.kafka.average.aggregation.model.CountAndSum;
import com.springboot.kafka.average.aggregation.serdes.JsonDeserializer;
import com.springboot.kafka.average.aggregation.serdes.JsonSerializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;

public class CountAndSumSerdes extends Serdes.WrapperSerde<CountAndSum> {
    public CountAndSumSerdes() {
        super (new JsonSerializer<>(),new JsonDeserializer<>(CountAndSum.class));
    }
    public static Serde<CountAndSum> serdes() {
        JsonSerializer<CountAndSum> serializer = new JsonSerializer<>();
        JsonDeserializer<CountAndSum> deSerializer = new JsonDeserializer<>(CountAndSum.class);
        return Serdes.serdeFrom(serializer, deSerializer);
    }
}
