package com.springboot.kafka.average.aggregation.processor;

import com.springboot.kafka.average.aggregation.model.MovieRating;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.streams.processor.TimestampExtractor;

import java.util.Optional;

public class MovieTimeExtractor implements TimestampExtractor {

    @Override
    public long extract(ConsumerRecord<Object, Object> record, long partitionTime) {
        MovieRating movieRating = (MovieRating) record.value();
        return Optional.ofNullable(movieRating.getTime())
                .map(it-> it.toInstant().toEpochMilli())
                .orElse(partitionTime);
    }
}
