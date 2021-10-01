package io.github.davidmerrick.confluentKafka.streams.util

import org.apache.kafka.clients.admin.Admin
import org.apache.kafka.clients.producer.Callback
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.clients.producer.RecordMetadata
import org.apache.kafka.common.serialization.StringSerializer
import java.util.Properties

object TopicLoader {
    @JvmStatic
    fun main(args: Array<String>) {
        runProducer()
    }

    fun runProducer() {
        val properties: Properties = StreamsUtils.loadProperties()
        properties[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
        properties[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
        Admin.create(properties).use { adminClient ->
            KafkaProducer<String, String>(properties).use { producer ->
                val inputTopic = properties.getProperty("basic.input.topic")
                val outputTopic = properties.getProperty("basic.output.topic")
                val topics = listOf(StreamsUtils.createTopic(inputTopic), StreamsUtils.createTopic(outputTopic))
                adminClient.createTopics(topics)
                val callback =
                    Callback { metadata: RecordMetadata, exception: Exception? ->
                        if (exception != null) {
                            System.out.printf("Producing records encountered error %s %n", exception)
                        } else {
                            System.out.printf(
                                "Record produced - offset - %d timestamp - %d %n",
                                metadata.offset(),
                                metadata.timestamp()
                            )
                        }
                    }
                val rawRecords = listOf(
                    "orderNumber-1001",
                    "orderNumber-5000",
                    "orderNumber-999",
                    "orderNumber-3330",
                    "bogus-1",
                    "bogus-2",
                    "orderNumber-8400"
                )
                rawRecords.map { ProducerRecord(inputTopic, "order-key", it) }
                    .forEach { producer.send(it, callback) }
            }
        }
    }
}
