# Overview

This project provides a simplified way to evaluate 
performance differences between [Apache Kafka](https://kafka.apache.org/) and [RabbitMQ streams](https://www.rabbitmq.com/streams.html).

The current version of only compares the publishing throughput of the 
[RabbitMQ](https://www.rabbitmq.com) and Kafka using [Spring Batch](https://spring.io/projects/spring-batch).


### Prerequisite

- [Java Version 17](https://jdk.java.net/17/)
- RabbitMQ Version 3.11 and highers
- [Apache Kafka](https://kafka.apache.org) version 3.5 and higher
- Postgres version 14 and higher (used for the Spring Batch job repository.)

## Building 

Use the maven ./mvnw to build the solution

```shell
./mvnw package
```

## Setup

Example Kafka Home directory

```shell
export KAFKA_HOME=/Users/devtools/integration/messaging/apacheKafka/kafka_2.13-3.5.1
```

| Step | Activity                                                                           | Examples/Script                                                                    |
|------|------------------------------------------------------------------------------------|------------------------------------------------------------------------------------|
| 1    | RabbitMQ - Setup [Download/Install](https://rabbitmq.com/download.html)            | ```brew install rabbitmq```                                                        |
| 2    | RabbitMQ -[Enable Stream Plugin](https://rabbitmq.com/stream.html#enabling-plugin) | ```rabbitmq-plugins enable rabbitmq_stream```                                      |
| 3    | Kafka -[Download Apache Kafka](https://kafka.apache.org/downloads)                 | See https://kafka.apache.org/quickstart                                            | 
| 4    | Kafka - Start Zookeeper                                                            | ```cd $KAFKA_HOME && bin/zookeeper-server-start.sh config/zookeeper.properties&``` |
| 5    | Kafka - Start Kafka Broker                                                         | ```cd $KAFKA_HOME && bin/kafka-server-start.sh config/server.properties &```       |
| 6    | Postgres - [Download/Install Postgres](https://www.postgresql.org/download/)       | ```brew install postgresql@14```                                                   |



## Generate Input File
Generate Input file with 2 Million Records

```shell
cd scripts/generate_batch_file
python generate_transaction_file.py
```

# Spring Batch Application 

Publish 2 million records


## RabbitMQ streams

Example

```shell
java -jar applications/rabbit-vs-kafka-batch/target/rabbit-vs-kafka-batch-0.0.1-SNAPSHOT.jar  --spring.profiles.active=rabbit --spring.rabbitmq.stream.uri=rabbitmq-stream://localhost:5552 --spring.rabbitmq.stream.name=transactions --spring.rabbitmq.stream.username=guest --spring.rabbitmq.stream.password=guest --spring.datasource.url=jdbc:postgresql://localhost:5432/postgres --spring.datasource.username=postgres --spring.datasource.password=
```

## Apache Kafka

Example 

```shell
java -jar applications/rabbit-vs-kafka-batch/target/rabbit-vs-kafka-batch-0.0.1-SNAPSHOT.jar  --spring.profiles.active=kafka --bootstrap.servers=localhost:9092 --kafka.producer.topic=transaction --spring.datasource.url=jdbc:postgresql://localhost:5432/postgres --spring.datasource.username=postgres --spring.datasource.password=
```



# Report App

You can use the [rabbit-vs-kafka-report-app](applications/rabbit-vs-kafka-report-app) to view the results.

## Run Application


Example

```shell
java -jar applications/rabbit-vs-kafka-report-app/target/rabbit-vs-kafka-report-app-0.0.1-SNAPSHOT.jar --spring.datasource.url=jdbc:postgresql://localhost:5432/postgres --spring.datasource.username=postgres --spring.datasource.password=
```


Open Browser

```shell
open http://localhost:8080
```

# Cleanup Data 

### Delete Kafka Topic

```shell
$KAFKA_HOME/bin/kafka-topics.sh --bootstrap-server=localhost:9092  --delete --topic transactions
```
### Delete RabbitMQ Stream

```shell
rabbitmqctl --node rabbit delete_queue transactions
```

### Delete Spring Batch Job Repository

```shell
psql -U postgres -d postgres -c 'DROP SCHEMA evt_stream CASCADE'
```

# Running