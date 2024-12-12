
## Kafka with a Kotlin example Project

## 1.	Add Kafka Dependencies

Include the following dependencies in your Kotlin project’s build.gradle.kts file:

```java
dependencies {
    implementation("org.apache.kafka:kafka-clients:3.5.0")
}
```

## 2.	Create a Kafka Producer

```java
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import java.util.*

fun main() {
    val props = Properties().apply {
        put("bootstrap.servers", "localhost:9092")
        put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
        put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    }

    val producer = KafkaProducer<String, String>(props)
    val topic = "test-topic"

    repeat(10) { i ->
        val message = "Message $i"
        val record = ProducerRecord(topic, "key-$i", message)
        producer.send(record) { metadata, exception ->
            if (exception == null) {
                println("Sent message: $message to ${metadata.topic()} partition ${metadata.partition()} offset ${metadata.offset()}")
            } else {
                exception.printStackTrace()
            }
        }
    }
    producer.close()
}
```

## 3.	Create a Kafka Consumer

```java
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import java.time.Duration
import java.util.*

fun main() {
    val props = Properties().apply {
        put("bootstrap.servers", "localhost:9092")
        put("group.id", "test-group")
        put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
        put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
        put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")
    }

    val consumer = KafkaConsumer<String, String>(props)
    val topic = "test-topic"
    consumer.subscribe(listOf(topic))

    while (true) {
        val records = consumer.poll(Duration.ofMillis(1000))
        for (record in records) {
            println("Consumed message: key=${record.key()}, value=${record.value()}, offset=${record.offset()}, partition=${record.partition()}")
        }
    }
}
```

## 4.	Run the Producer and Consumer

- Start the Consumer first to listen for messages.
- Run the Producer to send messages to the topic.
