package com.springboot.kafka.average.aggregation.serdeImpl;

import com.springboot.kafka.average.aggregation.model.MovieRating;
import com.springboot.kafka.average.aggregation.serdes.JsonDeserializer;
import com.springboot.kafka.average.aggregation.serdes.JsonSerializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;

public class MovieRatingSerdes extends Serdes.WrapperSerde<MovieRating> {
    public MovieRatingSerdes() {
        super (new JsonSerializer<>(),new JsonDeserializer<>(MovieRating.class));
    }
    public static Serde<MovieRating> serdes() {
        JsonSerializer<MovieRating> serializer = new JsonSerializer<>();
        JsonDeserializer<MovieRating> deSerializer = new JsonDeserializer<>(MovieRating.class);
        return Serdes.serdeFrom(serializer, deSerializer);
    }
}
