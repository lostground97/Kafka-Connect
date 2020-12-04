Kafka Connect REST Source connector

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

Building and running Spring example in docker
---
### 1 Installing dependencies and packaging the jar
```bash
mvn install
mvn clean package
```
### 2 Starting the Kafka, Zookeeper and Kafka connect environment
```bash
docker compose up -d
```
### 3 After all the connectors are up, install the custom connector
```bash
curl -X POST -H "Content-Type:application/json" -d @examples/basic-example.json http://localhost:8083/connectors
```

### 4 Open terminal and read log files generated by docker-compose. We need to read the lines where connect is mentioned.
```bash
docker-compose logs -f connect
```

### 5 Create a consumer and consume the data from the same topic.
```bash
docker exec kafka kafka-console-consumer --bootstrap-server kafka:9092 --topic *topic_name* --from-beginning
```
