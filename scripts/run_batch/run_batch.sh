#!/bin/bash

if [ "$#" -ne 2 ]; then
    echo "Usage $0 (rabbit|kafka) loopCount"
    exit;
fi

eventStream=$1
loopCount=$2

for i in $(seq "$loopCount"); do
  if [ "$eventStream" = "kafka" ];
  then
    java  -Xms1g -Xmx1g  -jar applications/rabbit-vs-kafka-batch/target/rabbit-vs-kafka-batch-0.0.1-SNAPSHOT.jar  --spring.profiles.active=kafka --bootstrap.servers=localhost:9092 --kafka.producer.topic=transaction --spring.datasource.url=jdbc:postgresql://localhost:5432/postgres --spring.datasource.username=postgres --spring.datasource.password=
  elif [ "$eventStream" = "rabbit" ];
  then
    java -Xms1g -Xmx1g -jar applications/rabbit-vs-kafka-batch/target/rabbit-vs-kafka-batch-0.0.1-SNAPSHOT.jar  --spring.profiles.active=rabbit --spring.rabbitmq.stream.uri=rabbitmq-stream://localhost:5552 --spring.rabbitmq.stream.name=transactions --spring.rabbitmq.stream.username=guest --spring.rabbitmq.stream.password=guest --spring.datasource.url=jdbc:postgresql://localhost:5432/postgres --spring.datasource.username=postgres --spring.datasource.password=
  else
    echo Invalid argument "$eventStream" != rabbit and "$eventStream" != kafka
    exit
  fi
done

