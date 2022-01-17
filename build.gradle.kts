/*
 * This file was generated by the Gradle 'init' task.
 *
 * This project uses @Incubating APIs which are subject to change.
 */

plugins {
    application
}

repositories {
    mavenLocal()
    maven {
        url = uri("https://mirrors.huaweicloud.com/repository/maven/")
    }
}

dependencies {
    implementation("com.diogonunes:JColor:5.2.0")
    implementation("net.java.dev.jna:jna:5.5.0")
    implementation("net.java.dev.jna:jna-platform:5.5.0")
    testImplementation("de.mirkosertic.bytecoder:java.desktop:2019-11-25")
}

group = "com.xfoss.learningJava"
version = "0.0.1"
description = "轻松学Java"
java.sourceCompatibility = JavaVersion.VERSION_1_8

application {
    mainClass.set("com.xfoss.BeatBox.MiniMusicCmdLine")
}

tasks {
    withType<JavaCompile>() {
        options.encoding = "UTF-8"
    }

    jar {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        manifest {
            attributes (mapOf("Main-Class" to "com.xfoss.BeatBox.MiniMusicCmdLine"))
        }
        from(configurations.runtimeClasspath.get().map {
            if (it.isDirectory) it else zipTree(it)
        })
        val sourcesMain = sourceSets.main.get()
        sourcesMain.allSource.forEach { println("add from sources: ${it.name}") }
        from(sourcesMain.output)
    }
}
