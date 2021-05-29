/*
 * Copyright (c) 2021. StulSoft
 */

/*
 * This file was generated by the Gradle 'init' task.
 *
 * This generated file contains a sample Java application project to get you started.
 * For more details take a look at the 'Building Java & JVM projects' chapter in the Gradle
 * User Manual available at https://docs.gradle.org/7.0.2/userguide/building_java_projects.html
 */

plugins {
    // Apply the application plugin to add support for building a CLI application in Java.
    application
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

val slf4jVersion = "1.7.30"
val log4jSlf4jVersion = "2.13.3"
val junitVersion = "5.7.1"
val vertxVersion = "4.0.3"


dependencies {
    // Vertx
    implementation("io.vertx:vertx-core:$vertxVersion")
    implementation("io.vertx:vertx-web:$vertxVersion")
    implementation("io.vertx:vertx-web-openapi:$vertxVersion")

    // Logging
    implementation("org.slf4j:slf4j-api:$slf4jVersion")
    implementation("org.slf4j:jcl-over-slf4j:$slf4jVersion")
    implementation("org.apache.logging.log4j:log4j-slf4j-impl:$log4jSlf4jVersion")

    // Tests
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

application {
    // Define the main class for the application.
    mainClass.set("com.stulsoft.pvertx.openapi1.App")
}

tasks.test {
    // Use junit platform for unit tests.
    useJUnitPlatform()
}
