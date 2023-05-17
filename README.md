# spring-boot kafka json stateful average aggregation
## Key Points
1. Generic Json Serializer and Deserialize as bottom level, Customized Json Serdes of MovieRating and CountSumAverage objects as Implementation layer, be able to support complex objects in aggregation to fillful complex statistic work at one time.
2. Using Spring boot configure the Kafka Stream, Consumer and Producer, defaultKafkaStreamsConfig for "kstream" processor, Spring KafkaAdmin and TopicBuilder for creating topics saftely before the processor running. 
4. Within the parenthase {} of "(key, value, aggregate)->{}", complete all average calculations and related output transform.
5. Materialize view saves aggregation result to KeyValueStore. Each movieId has only one unique record!   
6. Spring Rest API calls service of ReadOnlyKeyValueStore<Long,CountAndSum> to show stateful records by id.
7. Java Math.random for similating movie rating events, List<KayPair<Long,CountSumAverage>> show all or single record in the store
8. Using Confluent 6.0 images run single broker for demo

## Work flow chart

