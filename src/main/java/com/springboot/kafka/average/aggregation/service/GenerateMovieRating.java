package com.springboot.kafka.average.aggregation.service;

import com.springboot.kafka.average.aggregation.config.Constants;
import com.springboot.kafka.average.aggregation.model.MovieRating;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.junit.Test;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
@RequiredArgsConstructor
public class GenerateMovieRating {
    private  final KafkaTemplate<Long, MovieRating> kafkaTemplate;
    public static String movies[]={"Top Gun II","Gladiator","London Has Fallen","Blood Diamond","Troy"};
    public  List<MovieRating> createRandomMovieRation() {
        List<MovieRating> list = new ArrayList<>();

        for (Long i=0L; i<100L; i++) {
            Double r;
            r = 3+Math.random() * 7.0;
            int l = (int) (Math.random() * 4.0);

            Long id = Long.valueOf(l);
            MovieRating movieRating = new MovieRating(id,r,movies[l],new Date());
            list.add(movieRating);
        }
        return list;
    }

    public List<MovieRating> sendByRandomDurationSorted () {
        List<MovieRating> list =createRandomMovieRation();
        list.sort((o1,o2)->o1.getMovieId().compareTo(o2.getMovieId()));
        list.forEach(movieRating->{
            int l = (int) (Math.random() * 1000.0);  // millis seconds
            try {
                Thread.sleep(l);
            } catch (InterruptedException s) {}
            kafkaTemplate.send(Constants.INPUT_RATING_TOPIC,movieRating.getMovieId(),movieRating);
        });
        return list;
    }

    public List<MovieRating> sendByRandomDuration () {
        List<MovieRating> list =createRandomMovieRation();

        list.forEach(movieRating->{
            int l = (int) (Math.random() * 1000.0);  // millis seconds
            try {
                Thread.sleep(l);
            } catch (InterruptedException s) {}
            kafkaTemplate.send(Constants.INPUT_RATING_TOPIC,movieRating.getMovieId(),movieRating);
        });
        return list;
    }


}
