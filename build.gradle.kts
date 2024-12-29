plugins {
    id("java")
    id("application")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("commons-cli:commons-cli:1.5.0")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

java {
    sourceCompatibility = JavaVersion.VERSION_18
    targetCompatibility = JavaVersion.VERSION_18
}

application {
    mainClass.set("filefilterer.FileFilterer") // Указание главного класса
}

tasks.withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>().configureEach {
    archiveFileName.set("FileFilterer.jar")
    mergeServiceFiles()
    manifest {
        attributes["Main-Class"] = "filefilterer.FileFilterer"
    }
}

tasks.test {
    useJUnitPlatform()
}
