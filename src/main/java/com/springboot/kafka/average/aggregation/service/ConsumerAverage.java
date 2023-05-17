package com.springboot.kafka.average.aggregation.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.springboot.kafka.average.aggregation.model.MovieRating;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import com.springboot.kafka.average.aggregation.config.Constants;
@Component
@Slf4j
public class ConsumerAverage {
    /*
    @KafkaListener(topics = Constants.OUTPUT_RATING_AVERAGE_TOPIC,groupId=Constants.CONSUMER_GROUP_ID)
    public void listenBankBalance(ConsumerRecord<Long, MovieRating> record)  {
        log.info("Consumer Listened Average Movie Rating with Key: " + record.key() + ", Value: " + record.value());
    }*/
}
