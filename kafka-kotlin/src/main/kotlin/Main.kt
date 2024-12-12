package com.naveedkamran.examples.kafkakotlin

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import java.time.Duration
import java.time.LocalDateTime.now
import java.util.*

fun main() {
    println("Starting kafka demo!")
    val thread1 = Thread { startConsumer() }
    val thread2 = Thread { startProducer() }

    thread1.start()
    thread2.start()

    thread1.join() // Wait for thread1 to finish
    thread2.join() // Wait for thread2 to finish

    println("Program finished")
}

fun startProducer() {
    println("Starting producer")

    val props = Properties().apply {
        put("bootstrap.servers", "localhost:9092")
        put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
        put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    }

    val producer = KafkaProducer<String, String>(props)
    val topic = "test-topic"

    var i = 0
    while(true) {
        val message = "Message $i"
        val record = ProducerRecord(topic, "key-$i", message)
        producer.send(record) { metadata, exception ->
            if (exception == null) {
                println("${now()} Sent message: $message to ${metadata.topic()} partition ${metadata.partition()} offset ${metadata.offset()}")
            } else {
                exception.printStackTrace()
            }
        }
        Thread.sleep(2000)
        i++
    }
    producer.close()
    println("Producer closed")
}

fun startConsumer() {
    println("Starting consumer")

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
            println("${now()} Consumed message: key=${record.key()}, value=${record.value()}, offset=${record.offset()}, partition=${record.partition()}")
        }
    }

    println("Consumer closed")
}