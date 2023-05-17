package com.springboot.kafka.average.aggregation.config;

import com.springboot.kafka.average.aggregation.model.MovieRating;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.processor.WallclockTimestampExtractor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@EnableKafkaStreams
@EnableKafka
@Configuration
public class AverageConfigure {
    @Bean(name=Constants.BOOTSTRAP_ADMIN)
    public KafkaAdmin admin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, Constants.BOOSTRAP_SERVER);
        return new KafkaAdmin(configs);
    }
    @Bean(name=Constants.INPUT_RATING_TOPIC)
    @DependsOn(Constants.BOOTSTRAP_ADMIN)
    public NewTopic newTopic1() {
        return TopicBuilder.name(Constants.INPUT_RATING_TOPIC)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean(name=Constants.OUTPUT_RATING_AVERAGE_TOPIC)
    @DependsOn(Constants.INPUT_RATING_TOPIC)
    public NewTopic newTopic2() {
        return TopicBuilder.name(Constants.OUTPUT_RATING_AVERAGE_TOPIC)
                .partitions(1)
                .replicas(1)
                .build();
    }
    @Bean(name= KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
    @DependsOn(Constants.OUTPUT_RATING_AVERAGE_TOPIC)
    public KafkaStreamsConfiguration kafkaStreamsConfiguration() {
        Map<String,Object> properties = new HashMap<>();
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG, Constants.APPLICATION_ID_CONFIG);
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, Constants.BOOSTRAP_SERVER);
        properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.Long().getClass());
        properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.Double().getClass());
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        properties.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, "0");
        properties.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 10 * 1000);
        // For illustrative purposes we disable record caches.
        properties.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, 0);
        properties.put(StreamsConfig.DEFAULT_TIMESTAMP_EXTRACTOR_CLASS_CONFIG, WallclockTimestampExtractor.class.getName());
        return new KafkaStreamsConfiguration(properties);
    }

    @Bean
    @DependsOn(Constants.OUTPUT_RATING_AVERAGE_TOPIC)
    public ProducerFactory<Long, MovieRating> producerFactory() {
        final Map<String ,Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, Constants.BOOSTRAP_SERVER);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<Long, MovieRating>(config);
    }
    @Bean
    @DependsOn(Constants.OUTPUT_RATING_AVERAGE_TOPIC)
    public KafkaTemplate<Long,MovieRating> kafkaTemplate(final ProducerFactory producerFactory) {
        return new KafkaTemplate<>(producerFactory());
    }
    @Bean
    @DependsOn("consumerFactoryId")
    public ConcurrentKafkaListenerContainerFactory<Long, MovieRating> kafkaListenerContainerFactoryMovieRating (final ConsumerFactory<Long,MovieRating> consumerFactory) {
        final ConcurrentKafkaListenerContainerFactory<Long,MovieRating> factory = new ConcurrentKafkaListenerContainerFactory();
        factory.setConsumerFactory(consumerFactoryMovieRating());
        return factory;
    }

    @Bean(name="consumerFactoryId")
    @DependsOn(Constants.OUTPUT_RATING_AVERAGE_TOPIC)
    public ConsumerFactory<Long, MovieRating> consumerFactoryMovieRating() {
        final Map<String ,Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, Constants.BOOSTRAP_SERVER);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, Constants.CONSUMER_GROUP_ID);
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        // trust model package, critical step
        config.put(JsonDeserializer.TRUSTED_PACKAGES,"com.com.springboot.kafka.average.aggregation.model.MovieRating");
        return new DefaultKafkaConsumerFactory<Long,MovieRating>(config, new LongDeserializer(),
                new com.springboot.kafka.average.aggregation.serdes.JsonDeserializer<MovieRating>(MovieRating.class));
    }
}
