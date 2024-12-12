# Run Kafka in a Docker Container using docker-compose

Here’s a step-by-step tutorial to set up Apache Kafka on macOS using Docker and Docker Compose, including creating a Kafka topic and integrating it with a Kotlin project.

## Step 1: Prerequisites

Ensure Docker is installed and running on your machine. Also make sure that docker-compose is there on your machine

## Step 2: Create a Docker Compose File

Create a docker-compose.yml file for Kafka and Zookeeper setup. Zookeeper is a dependency for Kafka. The example docker-compose file is included in this directory.


## Step 3: Start Kafka

Run the following command in the directory where the docker-compose.yml file is saved:

```bash
docker-compose up -d
```

This will start both Zookeeper and Kafka in Docker containers.

## Step 4: Create a Kafka Topic

1.	Open a shell inside the Kafka container:
```
docker exec -it kafka bash
```

2.	Create a topic named test-topic:

```bash
kafka-topics --create \
  --bootstrap-server localhost:9092 \
  --replication-factor 1 \
  --partitions 1 \
  --topic test-topic
```

3.	List the topics to verify:

```bash
kafka-topics --list --bootstrap-server localhost:9092
```

To delete a topic you can use command:

```bash
kafka-topics --delete \
  --bootstrap-server localhost:9092 \
  --topic test-topic
```

## Step 5: Clean Up

Stop the Docker containers when you’re done:

```bash
docker-compose down
```

Let me know if you’d like further clarification or help!
