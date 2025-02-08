plugins {
    java
    application
    `maven-publish`
    id("io.freefair.lombok") version "8.12.1"
}

group = "io.github.ChindeaYTB"
version = "1.0.7"

allprojects {
    apply(plugin = "java")
    tasks.withType(JavaCompile::class) {
        sourceCompatibility = "1.8"
        targetCompatibility = "1.8"
    }
    repositories {
        mavenCentral()
    }
}

dependencies {
    implementation("com.google.code.gson:gson:2.2.4")
}

project(":updater") {
    tasks.jar {
        archiveFileName.set("updater.jar")
        manifest {
            attributes(
                mapOf(
                    "Main-Class" to "io.github.chindeaytb.updatermain.UpdaterMain"
                )
            )
        }
    }
}

java {
    withJavadocJar()
    withSourcesJar()
}
tasks.javadoc {
    isFailOnError = false
}

tasks.processResources {
    val updateJar = tasks.getByPath(":updater:jar")
    from(updateJar.outputs)
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "io.github.ChindeaYTB"
            artifactId = "modrinthautoupdater"
            version = "1.0.7"

            from(components["java"])
        }
    }
}

