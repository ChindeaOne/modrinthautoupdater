plugins {
    java
    application
    `maven-publish`
}

group = "io.github.ChindeaYTB"
version = "1.0.5"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.code.gson:gson:2.2.4")
}

application {
    mainClass.set("io.github.chindeaytb.Main")
}

tasks.jar {
    manifest {
        attributes(
            "Main-Class" to "io.github.chindeaytb.Main"
        )
    }
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
}

val updaterJar = tasks.register<Jar>("updaterJar") {
    archiveFileName.set("updater.jar")
    destinationDirectory.set(layout.buildDirectory.dir("libs"))
    from(sourceSets.main.get().output)
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
    manifest {
        attributes("Main-Class" to "io.github.chindeaytb.UpdaterMain")
    }
}

tasks.build {
    dependsOn(updaterJar)
}

tasks.jar {
    dependsOn(updaterJar)
    doFirst {
        val updaterJarFile = updaterJar.get().archiveFile.get().asFile
        if (!updaterJarFile.exists()) {
            throw GradleException("Updater JAR was not built successfully.")
        }
    }
    from(updaterJar.get().outputs.files) {
        into("updater")
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "io.github.ChindeaYTB"
            artifactId = "modrinthautoupdater"
            version = "1.0.5"

            from(components["java"])
        }
    }
}

