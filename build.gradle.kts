plugins {
    id("fabric-loom") version "0.10.63"
    `maven-publish`
}

group = "me.ramidzkh"
version = "1.0.0-SNAPSHOT"

dependencies {
    minecraft("net.minecraft", "minecraft", "1.18")
    mappings("net.fabricmc", "yarn", "1.18+build.1", classifier = "v2")
    modImplementation("net.fabricmc", "fabric-loader", "0.12.8")

    modImplementation("net.fabricmc.fabric-api", "fabric-api", "0.43.1+1.18")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks {
    withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.release.set(17)
    }

    withType<AbstractArchiveTask> {
        from(rootProject.file("LICENSE"))
    }

    processResources {
        inputs.property("version", project.version)

        filesMatching("fabric.mod.json") {
            expand("version" to project.version)
        }
    }

    jar {
        from(rootProject.file("LICENSE"))
    }
}

publishing {
    publications {
        create<MavenPublication>("mod") {
            from(components["java"])
        }
    }

    repositories {
        maven {
            val releasesRepoUrl = uri("${buildDir}/repos/releases")
            val snapshotsRepoUrl = uri("${buildDir}/repos/snapshots")
            name = "Project"
            url = if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl
        }
    }
}
