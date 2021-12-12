plugins {
    id("fabric-loom") version "0.10.63"
    `maven-publish`
}

group = "me.ramidzkh"
version = "1.0.0-SNAPSHOT"

repositories {
    maven {
        name = ":tiny_potato:"
        url = uri("https://maven.tiny-potato.xyz/")
    }

    maven {
        name = "shedaniel"
        url = uri("https://maven.shedaniel.me/")
    }
}

sourceSets {
    main {
        resources {
            srcDir("src/main/generated")
        }
    }
}

dependencies {
    minecraft("net.minecraft", "minecraft", "1.18.1")
    mappings("net.fabricmc", "yarn", "1.18.1+build.2", classifier = "v2")
    modImplementation("net.fabricmc", "fabric-loader", "0.12.11")

    modImplementation("net.fabricmc.fabric-api", "fabric-api", "0.44.0+1.18")
    modImplementation("io.github.astrarre", "astrarre-gui-v1-fabric", "1.0.14")
    modImplementation("teamreborn", "energy", "2.0.0-beta1") {
        exclude(group = "net.fabricmc.fabric-api")
    }

    include("teamreborn", "energy", "2.0.0-beta1")
}

loom {
    runs {
        create("datagen") {
            client()

            configName = "Data Generation"
            runDir = "build/datagen"

            vmArg("-Dfabric-api.datagen")
            vmArg("-Dfabric-api.datagen.output-dir=${file("src/main/generated")}")
        }
    }
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

    processResources {
        inputs.property("version", project.version)

        filesMatching("fabric.mod.json") {
            expand("version" to project.version)
        }
    }

    jar {
        from(project.file("LICENSE"))
    }

    assemble {
        val runDatagen by this@tasks
        dependsOn(runDatagen)
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
