package io.github.davidmerrick.confluentKafka.streams.lesson4

import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.StreamsConfig
import org.apache.kafka.streams.kstream.KTable
import org.apache.kafka.streams.kstream.Materialized
import java.io.File
import java.util.Properties


class Lesson4 {
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

            val builder = StreamsBuilder()
            val firstKTable: KTable<String, String> = builder.table(
                inputTopic,
                Materialized.with(Serdes.String(), Serdes.String())
            )


        }
    }
}
