group = "io.github.davidmerrick.confluentKafka"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
    maven(url = "https://packages.confluent.io/maven")
}

plugins {
    kotlin("jvm") version "1.5.20"
    id("com.github.davidmc24.gradle.plugin.avro") version "1.0.0"
}

dependencies {
    implementation("org.apache.avro:avro:1.10.2")
    implementation("org.apache.kafka:kafka-streams:2.8.0") {
        exclude(group = "org.apache.kafka", module = "kafka-clients")
    }
    implementation("org.apache.kafka:kafka-clients:2.8.0!!")
    implementation("io.confluent:kafka-streams-avro-serde:6.1.1") {
        exclude(group = "org.apache.kafka", module = "kafka-clients")
        exclude(group = "org.apache.kafka", module = "kafka-streams")
    }
    implementation("org.slf4j:slf4j-api:1.7.26")
    implementation("org.slf4j:slf4j-jdk14:1.7.26")
    implementation("io.github.microutils:kotlin-logging:1.7.2")

    testImplementation("org.apache.kafka:kafka-streams-test-utils:2.8.0")
    testImplementation("org.testng:testng:7.1.0")
    testImplementation("io.kotlintest:kotlintest-runner-junit5:3.4.2")
}

tasks {
    test {
        useTestNG()
    }

    compileKotlin {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }

    compileTestKotlin {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }
}
