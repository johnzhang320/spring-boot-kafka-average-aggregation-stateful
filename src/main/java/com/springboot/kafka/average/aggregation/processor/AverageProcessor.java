package com.springboot.kafka.average.aggregation.processor;

import com.springboot.kafka.average.aggregation.config.Constants;
import com.springboot.kafka.average.aggregation.model.CountSumAverage;
import com.springboot.kafka.average.aggregation.model.MovieRating;
import com.springboot.kafka.average.aggregation.serdeImpl.CountAndSumSerdes;
import com.springboot.kafka.average.aggregation.serdeImpl.MovieRatingSerdes;
import com.springboot.kafka.average.aggregation.service.GenerateMovieRating;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.KeyValueStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;

import static org.apache.kafka.streams.kstream.Grouped.with;
import static org.apache.kafka.common.serialization.Serdes.Double;
import static org.apache.kafka.common.serialization.Serdes.Long;
@Configuration
@EnableKafka
@EnableKafkaStreams
@Slf4j
public class AverageProcessor {

   //@Bean
    public KStream<Long, CountSumAverage> kStream(StreamsBuilder streamsBuilder) {
        KStream<Long, MovieRating> moveRatingStream = streamsBuilder.stream(Constants.INPUT_RATING_TOPIC,
                Consumed.with(Long(), MovieRatingSerdes.serdes())
                        .withTimestampExtractor(new MovieTimeExtractor()));

        KGroupedStream<Long, Double> ratingsById = moveRatingStream
                .map((key, rating) -> new KeyValue<>(rating.getMovieId(), rating.getRating()))
                .groupByKey(with(Long(), Double()));

        final KTable<Long, CountSumAverage> ratingCountAndSum =
                ratingsById.aggregate(() -> new CountSumAverage(),  // initial when create instance
                        (key, value, aggregate) -> {
                            aggregate.setMovieId(key);
                            aggregate.setCount(aggregate.getCount() + 1);
                            aggregate.setSum(aggregate.getSum() + value);
                            aggregate.setAverage(aggregate.getCount()>0 ? aggregate.getSum()/aggregate.getCount():0);
                            aggregate.setMovieName(GenerateMovieRating.movies[Integer.valueOf(Math.toIntExact(key))]);
                            return aggregate;
                        },
                        Materialized.<Long, CountSumAverage, KeyValueStore<Bytes,byte[]>>as(Constants.MOVIE_STORE)
                                .withKeySerde(Long())
                                .withValueSerde(CountAndSumSerdes.serdes()));

        KStream<Long, CountSumAverage> retResult=ratingCountAndSum.toStream()
                .peek((key,value)->log.info("Average Movie Rating Id {}, Average Rating: {}, Movie Name {}",key,value.getAverage(),value.getMovieName()));



        return retResult;

    }
}
