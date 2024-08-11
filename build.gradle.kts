import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import java.text.SimpleDateFormat
import java.util.*

plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("org.sonarqube") version "5.0.0.4638"
}

group = "xyz.sorridi.arc"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven {
        url = uri("https://repo.spring.io/plugins-release/")
    }
}

val junitBomVersion = "5.10.3"
val sparkJavaVersion = "2.9.4"
val slf4jVersion = "2.0.16"
val jettyWebsocketVersion = "9.4.55.v20240627"
val bucket4jVersion = "8.10.1"
val bouncycastleVersion = "1.78.1"

val commonsCodecVersion = "1.17.1"
val commonsCliVersion = "1.8.0"
val commonsLang3Version = "3.16.0"
val commonsTextVersion = "1.12.0"

val gsonVersion = "2.11.0"
val lombokVersion = "1.18.34"
val annotationsVersion = "24.1.0"

dependencies {
    testImplementation(platform("org.junit:junit-bom:$junitBomVersion"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    implementation("com.sparkjava:spark-core:$sparkJavaVersion")
    implementation("org.slf4j:slf4j-nop:$slf4jVersion")
    implementation("org.eclipse.jetty.websocket:websocket-server:$jettyWebsocketVersion")
    implementation("com.bucket4j:bucket4j-core:$bucket4jVersion")
    implementation("org.bouncycastle:bcpkix-jdk18on:$bouncycastleVersion")
    implementation("commons-codec:commons-codec:$commonsCodecVersion")
    implementation("commons-cli:commons-cli:$commonsCliVersion")
    implementation("org.apache.commons:commons-lang3:$commonsLang3Version")
    implementation("org.apache.commons:commons-text:$commonsTextVersion")
    implementation("com.google.code.gson:gson:$gsonVersion")

    /* Annotations related. */
    compileOnly("org.projectlombok:lombok:$lombokVersion")
    compileOnly("org.jetbrains:annotations:$annotationsVersion")

    annotationProcessor("org.projectlombok:lombok:$lombokVersion")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<ShadowJar> {
    archiveFileName.set("${project.name}-${project.version}.jar")
    manifest {
        attributes(
            "Main-Class" to "xyz.sorridi.arc.Main",
            "Built-By" to System.getProperty("user.name"),
            "Build-Timestamp" to SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(Date()),
            "Created-By" to "Gradle ${gradle.gradleVersion}",
            "Build-Jdk" to "${System.getProperty("java.version")} (${System.getProperty("java.vendor")} ${
                System.getProperty(
                    "java.vm.version"
                )
            })",
            "Build-OS" to "${System.getProperty("os.name")} ${System.getProperty("os.arch")} ${System.getProperty("os.version")}"
        )
    }
}
