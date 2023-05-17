package com.springboot.kafka.average.aggregation.service;

import com.springboot.kafka.average.aggregation.config.Constants;
import com.springboot.kafka.average.aggregation.model.CountSumAverage;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@EnableKafka
@EnableKafkaStreams
public class RetrieveRatingAverageService {

    private StreamsBuilderFactoryBean streamsBuilderFactoryBean;
    @Autowired
    public RetrieveRatingAverageService(StreamsBuilderFactoryBean streamsBuilderFactoryBean) {
        this.streamsBuilderFactoryBean = streamsBuilderFactoryBean;
    }
    public CountSumAverage getStoreCountAndSum(Long movieId) {
        return initializeStore().get(movieId);
    }
    public List<KeyValue<Long, CountSumAverage>> getAllKeyValueStores() {
        List<KeyValue<Long, CountSumAverage>> list = new ArrayList<>();
        ReadOnlyKeyValueStore <Long, CountSumAverage> keyValueStores = this.initializeStore();
        KeyValueIterator<Long, CountSumAverage> keyValueIterator = keyValueStores.all();
        while (keyValueIterator.hasNext()) {
            list.add(keyValueIterator.next());
        }
        return list;
    }
    private ReadOnlyKeyValueStore<Long, CountSumAverage> initializeStore() {
        KafkaStreams kafkaStreams = streamsBuilderFactoryBean.getKafkaStreams();
        return kafkaStreams.store(
                StoreQueryParameters.fromNameAndType(
                        Constants.MOVIE_STORE,
                        QueryableStoreTypes.keyValueStore()
                )
        );
    }
}
