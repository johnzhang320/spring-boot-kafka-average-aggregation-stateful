package com.springboot.kafka.average.aggregation.serdeImpl;

import com.springboot.kafka.average.aggregation.model.CountSumAverage;
import com.springboot.kafka.average.aggregation.serdes.JsonDeserializer;
import com.springboot.kafka.average.aggregation.serdes.JsonSerializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;

public class CountAndSumSerdes extends Serdes.WrapperSerde<CountSumAverage> {
    public CountAndSumSerdes() {
        super (new JsonSerializer<>(),new JsonDeserializer<>(CountSumAverage.class));
    }
    public static Serde<CountSumAverage> serdes() {
        JsonSerializer<CountSumAverage> serializer = new JsonSerializer<>();
        JsonDeserializer<CountSumAverage> deSerializer = new JsonDeserializer<>(CountSumAverage.class);
        return Serdes.serdeFrom(serializer, deSerializer);
    }
}
