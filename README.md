# Overview

This project provides a simplified way to evaluate 
performance differences between Apache Kafka and RabbitMQ streams

### Prerequisite

- [Java Version 17](https://jdk.java.net/17/)
- RabbitMQ Version 3.11 and highers
- [Apache Kafka](https://kafka.apache.org) version 3.5 and higher
- Postgres version 14 and higher (used for the Spring Batch job repository.)

## Setup


Example Kafka Home directory

```shell
export KAFKA_HOME=/Users/devtools/integration/messaging/apacheKafka/kafka_2.13-3.5.1
```

| Step | Activity                                                                           | Examples/Script                                                                                                                                                           |
|------|------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| 1    | RabbitMQ - Setup [Download/Install](https://rabbitmq.com/download.html)            | ```brew install rabbitmq```                                                                                                                                               |
| 2    | RabbitMQ -[Enable Stream Plugin](https://rabbitmq.com/stream.html#enabling-plugin) | ```rabbitmq-plugins enable rabbitmq_stream```                                                                                                                             |
| 3    | Kafka -[Download Apache Kafka](https://kafka.apache.org/downloads)                 | See https://kafka.apache.org/quickstart                                                                                                                                   | 
| 4    | Kafka - Start Zookeeper                                                            | ```cd $KAFKA_HOME && bin/zookeeper-server-start.sh config/zookeeper.properties``` |
| 5    | Kafka - Start Kafka Broker                                                         | ```cd $KAFKA_HOME &&bin/kafka-server-start.sh config/server.properties```         |
| 6    | Postgres - [Download/Install Postgres](https://www.postgresql.org/download/)       | ```brew install postgresql@14```                                                                                                                                          |



## Generate Input File
Generate Input file with 2 Million Records

```shell
cd scripts/generate_batch_file
python generate_transaction_file.py
```

# Running RabbitMQ Batch

Create an Application Properties

Example

```properties
spring.application.name=rabbit-vs-kafka-batch
batch.file.location=scripts/generate_batch_file/runtime/transactions.csv
batch.chunk.size=10000
rabbitmq.stream.name=transaction
spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
spring.datasource.username=<USERNAME>
spring.datasource.password=<POSTGRES>
rabbitmq.stream.producer.batch.size=10000
batch.core.pool.size=16
```



