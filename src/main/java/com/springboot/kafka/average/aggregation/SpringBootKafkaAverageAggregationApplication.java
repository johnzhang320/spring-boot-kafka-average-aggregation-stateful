package com.springboot.kafka.average.aggregation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;

@SpringBootApplication
@EnableKafka
@EnableKafkaStreams
public class SpringBootKafkaAverageAggregationApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootKafkaAverageAggregationApplication.class, args);
	}

}
