# spring-boot-kafka-json-statful-average-aggregation
## Key Points
1. Customized Json Serializer and Deserialize as bottom level, MovieRating and CountAndSum Json Serdes as Implementation layer, which supports any complex model objects
2. Using Spring boot configure the Kafka Stream, Json Objcet Consumer and Producer, using defaultKafkaStreamsConfig for KStream processor (Similiar to Confluent's Topology), using Spring boot KafkaAdmin and TopicBuilder to create topic use @DependsOn to depend on previous @Bean to gaurantee creating topic before processor running. Therefore no more need docker-compose.yml or manually creating topics
4. When we do aggregation, after initializing CountAndSum object, within the parenthase {} of "aggregate->{}", complete all calculations of count, sum, average and also setting movieId and movie name to CountAndSum. No need extra KTable transform to calculating average.
5. Create Materialize view and save to keyvalueStore by movieId, found kayvaluestore uniquely saving each movie average by movieId, the result seems to be much better than relying on Time Window and Suppress because we can using API to find average from the store anytime, the result is super suppressed (to unique)
6. Using Apache KafkaStream.store return ReadOnlyKeyValueStore<Long,CountAndSum>  
7. Using Java Math.random method to similate movie rating events, Using Spring boot Rest API to access the Store
8. Using Confluent 6.0 images

## Work flow chart

