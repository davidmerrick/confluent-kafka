group = "io.github.davidmerrick.myProject"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

plugins {
    application
    kotlin("jvm") version "1.5.20"
}

application {
    mainClassName = "io.github.davidmerrick.MyProject"
}

dependencies {
    implementation(kotlin("script-runtime"))
    implementation("org.slf4j:slf4j-api:1.7.26")
    implementation("org.slf4j:slf4j-jdk14:1.7.26")
    implementation("io.github.microutils:kotlin-logging:1.7.2")

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
