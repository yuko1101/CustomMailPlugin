plugins {
    id("java")
}

group = properties["groupId"] as String
version = properties["version"] as String

repositories {
    mavenCentral()
    maven(url = "https://jitpack.io")
}

dependencies {
    implementation(group = "com.github.Melledy", name = "LunarCore", version = "development-SNAPSHOT")
}

tasks.jar {
    archiveBaseName.set(properties["archivesBaseName"] as String)
}