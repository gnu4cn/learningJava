/*
 * This file was generated by the Gradle 'init' task.
 *
 * This project uses @Incubating APIs which are subject to change.
 */
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import edu.sc.seis.launch4j.tasks.DefaultLaunch4jTask

application {
    // mainClassName = "com.xfoss.SimpleChat.VerySimpleChatServer"
    mainClassName = "com.xfoss.CollectionAndGenerics.TestGenerics1"
    getMainClass().set(mainClassName)
}

plugins {
    application
    kotlin("jvm") version "1.3.50"
    id("com.github.johnrengelman.shadow") version "5.1.0"
    id("edu.sc.seis.launch4j")  version "2.5.1"
}

repositories {
    flatDir {
        dirs("src/main/java")
    }
    mavenLocal()
    maven {
        url = uri("https://mirrors.huaweicloud.com/repository/maven/")
    }
}

dependencies {
    files("./")
    implementation("commons-io:commons-io:2.6")
    implementation("com.diogonunes:JColor:5.2.0")
    implementation("net.java.dev.jna:jna:5.5.0")
    implementation("net.java.dev.jna:jna-platform:5.5.0")
    testImplementation("de.mirkosertic.bytecoder:java.desktop:2019-11-25")
}

group = "Head First Java, Java 教程"
version = "0.0.1"
description = "轻松学Java"
java.sourceCompatibility = JavaVersion.VERSION_1_8

tasks {
    withType<JavaCompile>() {
        options.encoding = "UTF-8"
    }

    jar {
        manifest {
            attributes (mapOf("Main-Class" to application.mainClassName))
        }

        duplicatesStrategy = DuplicatesStrategy.EXCLUDE

        // archiveClassifier.set("uber")
        dependsOn(configurations.runtimeClasspath)
        from({
            configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) }
        })
        val sourcesMain = sourceSets.main.get()
        sourcesMain.allSource.forEach { 
            println("add from sources: ${it.name}") 
        }
        from(sourcesMain.output)
    }

}

tasks.withType<KotlinCompile> {
  kotlinOptions.jvmTarget = "1.8"
}
