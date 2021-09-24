package io.github.davidmerrick.confluentKafka.streams.util

import org.apache.kafka.clients.admin.NewTopic
import org.apache.kafka.clients.producer.Callback
import org.apache.kafka.clients.producer.RecordMetadata
import java.io.FileInputStream
import java.util.Properties

private const val REPLICATION_FACTOR: Short = 3
private const val PARTITIONS = 6
private const val PROPERTIES_FILE_PATH = "src/main/resources/streams.properties"

object StreamsUtils {
    fun loadProperties(): Properties {
        val properties = Properties()
        FileInputStream("src/main/resources/streams.properties").use { fis ->
            properties.load(fis)
            return properties
        }
    }

    fun propertiesToMap(properties: Properties): Map<String, Any> {
        val configs: MutableMap<String, Any> = HashMap()
        properties.forEach { key: Any, value: Any ->
            configs[key as String] = value as String
        }
        return configs
    }

    fun callback(): Callback {
        return Callback { metadata: RecordMetadata, exception: Exception? ->
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
    }

    fun createTopic(topicName: String?): NewTopic {
        return NewTopic(topicName, PARTITIONS, REPLICATION_FACTOR)
    }
}
