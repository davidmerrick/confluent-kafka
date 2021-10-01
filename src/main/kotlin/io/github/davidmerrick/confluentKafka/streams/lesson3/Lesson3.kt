package io.github.davidmerrick.confluentKafka.streams.lesson3

import io.github.davidmerrick.confluentKafka.streams.util.TopicLoader
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.KafkaStreams
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.StreamsConfig
import org.apache.kafka.streams.kstream.Consumed
import org.apache.kafka.streams.kstream.Produced
import java.io.File
import java.util.Properties


class Lesson3 {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val streamsProps = Properties()
            File(javaClass.getResource("/streams.properties").path)
                .inputStream()
                .use { streamsProps.load(it) }
            streamsProps[StreamsConfig.APPLICATION_ID_CONFIG] = "basic-streams"

            // Always start with a StreamsBuilder
            val builder = StreamsBuilder()

            // Stream is created based off a topic
            val inputTopic = streamsProps.getProperty("basic.input.topic")
            val outputTopic = streamsProps.getProperty("basic.output.topic")
            val orderNumberStart = "orderNumber-"

            // Must specify which serializer/deserializer to use
            val firstStream = builder.stream(inputTopic, Consumed.with(Serdes.String(), Serdes.String()))

            firstStream.peek { key, value -> println("Incoming record - key $key value $value") }
                // Filter only values which contain order number
                .filter { _, value -> value.contains(orderNumberStart) }
                .mapValues { value -> value.substring(value.indexOf("-") + 1) }
                .filter { _, value -> value.toLong() > 1000 }
                .peek { key, value -> println("Outgoing record - key $key value $value") }
                // Streams always feed down to a sink, which is an output topic
                .to(outputTopic, Produced.with(Serdes.String(), Serdes.String()))

            val kafkaStreams = KafkaStreams(builder.build(), streamsProps)
            TopicLoader.runProducer()
            kafkaStreams.start()
        }
    }
}
