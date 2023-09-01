# Running RabbitMQ

Setup [RabbitMQ](https://rabbitmq.com/) 

- [Download/Install](https://rabbitmq.com/download.html)
- [Enable Stream Plugin](https://rabbitmq.com/stream.html#enabling-plugin)

# Postgres Database


This project requires a Spring Batch job repository.
You can use Postgres by default.

-  [Download/Install Postgres](https://www.postgresql.org/download/)


# Generate Input File
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



