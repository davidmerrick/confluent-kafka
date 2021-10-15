package io.github.davidmerrick.confluentKafka.streams.lesson5

import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.common.utils.Bytes
import org.apache.kafka.streams.KafkaStreams
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.StreamsConfig
import org.apache.kafka.streams.kstream.Materialized
import org.apache.kafka.streams.kstream.Produced
import org.apache.kafka.streams.state.KeyValueStore
import java.io.File
import java.util.Properties
import io.github.davidmerrick.confluentKafka.streams.util.TopicLoader

const val ORDER_NUMBER_START = "orderNumber-"

class Lesson5 {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val streamsProps = Properties()
            File(javaClass.getResource("/streams.properties").path)
                .inputStream()
                .use { streamsProps.load(it) }
            streamsProps[StreamsConfig.APPLICATION_ID_CONFIG] = "basic-streams"

            // Stream is created based off a topic
            val inputTopic = streamsProps.getProperty("basic.input.topic")
            val outputTopic = streamsProps.getProperty("basic.output.topic")

            val builder = StreamsBuilder()
            val firstKTable = builder.table(
                inputTopic,
                Materialized.`as`<String, String, KeyValueStore<Bytes, ByteArray>>("ktable-store")
                    .withKeySerde(Serdes.String())
                    .withValueSerde(Serdes.String())
            )

            firstKTable.filter { _, value -> value.contains(ORDER_NUMBER_START) }
                .mapValues { value -> value.substring(value.indexOf("-") + 1) }
                .filter { _, value -> value.toLong() > 1000 }
                .toStream()
                .peek { key, value -> println("Outgoing record - key $key value $value") }
                .to(outputTopic, Produced.with(Serdes.String(), Serdes.String()))
            KafkaStreams(builder.build(), streamsProps)
            TopicLoader.runProducer()
        }
    }
}
