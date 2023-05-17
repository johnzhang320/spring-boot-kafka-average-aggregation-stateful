package com.springboot.kafka.average.aggregation.control;

import com.springboot.kafka.average.aggregation.model.CountAndSum;
import com.springboot.kafka.average.aggregation.model.MovieRating;
import com.springboot.kafka.average.aggregation.service.GenerateMovieRating;
import com.springboot.kafka.average.aggregation.service.RetrieveRatingAverageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.state.KeyValueStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/movie")
@RestController
@RequiredArgsConstructor
@Slf4j
public class MovieController {
    private final GenerateMovieRating generateMockRating;
    private final RetrieveRatingAverageService retrieveRatingAverageService;
    // http://localhost:8098/movie/sendRandomRatingSorted
    @GetMapping("/sendRandomRatingSorted")
    public List<MovieRating> startAggregationSorted() {
      return generateMockRating.sendByRandomDurationSorted();
    }

    // http://localhost:8098/movie/sendRandomRating
    @GetMapping("/sendRandomRating")
    public List<MovieRating> startAggregation() {
        return generateMockRating.sendByRandomDuration();
    }
    // http://localhost:8098/movie/getAverageRating/1
    @GetMapping("/getAverageRating/{movieId}")
    public CountAndSum retriveMovieStore(@PathVariable("movieId") Long movieId) {
        CountAndSum countAndSum = retrieveRatingAverageService.getStoreCountAndSum(movieId);
        log.info(countAndSum.toString());
        return countAndSum;
    }
    // http://localhost:8098/movie/getAllAverageRating
    @GetMapping("/getAllAverageRating")
    public List<KeyValue<Long,CountAndSum>> retriveAllMovieAverageStore() {
        return retrieveRatingAverageService.getAllKeyValueStores();
    }
}
