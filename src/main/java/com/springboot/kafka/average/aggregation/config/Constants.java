package com.springboot.kafka.average.aggregation.config;

public interface Constants {
    public static final String BOOSTRAP_SERVER="localhost:29092";
    public static final String CONSUMER_GROUP_ID="consumer-group";

    public static final String APPLICATION_ID_CONFIG="average-calculate";

    public static final String INPUT_RATING_TOPIC = "movie-ratings-topic";

    public static final String MOVIE_STORE= "movie-store";

    public static final String AVERAGE_MOVIE_STORE= "average-ratings";

    public static final String AVERAGE_MOVIE_STORE_KTABLE= "average-ratings-ktable";

    public static final String OUTPUT_RATING_AVERAGE_TOPIC="movie-average-topic";

    public static final String BOOTSTRAP_ADMIN="boostrapServerAdmin";
}
