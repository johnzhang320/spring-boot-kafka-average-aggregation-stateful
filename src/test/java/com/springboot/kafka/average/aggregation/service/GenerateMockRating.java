package com.springboot.kafka.average.aggregation.service;

import com.springboot.kafka.average.aggregation.config.Constants;
import com.springboot.kafka.average.aggregation.model.MovieRating;
import kafka.Kafka;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import scala.math.package$;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class GenerateMockRating {
    private final KafkaTemplate<Long, MovieRating> kafkaTemplate;
    public static List<MovieRating> generateMovieRation() {
        List<MovieRating> list = new ArrayList<>();
        String movies[]={"Top Gun II","Gladiator","London Has Fallen","Blood Diamond","Troy"};
        for (Long i=100L; i<1000L; i++) {
            Double r = 4.5+Math.random() * 5.0;
            int l = (int) (Math.random() * 4.0);
            Long id = Long.valueOf(l);
            MovieRating movieRating = new MovieRating(id,r,movies[l]);
            list.add(movieRating);
        }
        return list;
    }

    public  void sendMovieRation(String[] args) {
        List<MovieRating> list=generateMovieRation();
        list.forEach(mr-> {
            kafkaTemplate.send(Constants.INPUT_RATING_TOPIC,mr.getMovieId(),mr);
        });
    }
}
