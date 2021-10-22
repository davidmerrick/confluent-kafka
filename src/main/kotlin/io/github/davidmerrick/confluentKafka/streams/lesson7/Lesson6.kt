package io.github.davidmerrick.confluentKafka.streams.lesson7

import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.StreamsConfig
import org.apache.kafka.streams.kstream.JoinWindows
import org.apache.kafka.streams.kstream.KStream
import org.apache.kafka.streams.kstream.ValueJoiner
import java.io.File
import java.time.Duration
import java.util.Properties


class Lesson7 {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val streamsProps = Properties()
            File(javaClass.getResource("/streams.properties").path)
                .inputStream()
                .use { streamsProps.load(it) }
            streamsProps[StreamsConfig.APPLICATION_ID_CONFIG] = "basic-streams"

            val builder = StreamsBuilder()
            val leftStream: KStream<String, String> = builder.stream("topic-A")
            val rightStream: KStream<String, String> = builder.stream("topic-B")

            val valueJoiner =
                ValueJoiner { leftValue: String, rightValue: String -> leftValue + rightValue }
            leftStream.join(
                rightStream,
                valueJoiner,
                JoinWindows.of(Duration.ofSeconds(10))
            )
        }
    }
}
