import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.10"
}

group = "com.yellowsunn"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.reactivex.rxjava2:rxkotlin:2.4.0")
//    implementation("io.github.microutils:kotlin-logging:3.0.2")
    implementation("org.slf4j:slf4j-api:2.0.3")
    implementation("ch.qos.logback:logback-classic:1.4.4")

    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.9.0")

}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}