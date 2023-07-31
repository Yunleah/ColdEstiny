plugins {
    java
    id("io.izzel.taboolib") version "1.56"
    id("org.jetbrains.kotlin.jvm") version "1.7.20"
}

taboolib {
    install("common")
    install("common-5")
    install("module-chat")
    install("module-configuration")
    install("module-kether")
    install("module-lang")
    install("module-metrics")
    install("platform-bukkit")
    install("expansion-command-helper")
    install("module-nms")
    install("module-nms-util")
    classifier = null
    version = "6.0.11-13"

    description {
        contributors {
            name("Yunleah")
        }
        desc("A Bukkit Death Engine for TabooLib")
        dependencies {
            name("Residence").with("bukkit").optional(true)
            name("GriefDefender").with("bukkit").optional(true)
            name("WorldGuard").with("bukkit").optional(true)
            name("PlaceholderAPI").with("bukkit").optional(true)
        }
    }
}

repositories {
    mavenCentral()
    maven("https://repo.glaremasters.me/repository/bloodshot")
    maven("https://maven.enginehub.org/repo/")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
}

dependencies {
    compileOnly("ink.ptms:nms-all:1.0.0")
    compileOnly("ink.ptms.core:v11902:11902-minimize:mapped")
    compileOnly("ink.ptms.core:v11902:11902-minimize:universal")
    compileOnly("com.griefdefender:api:2.1.0-SNAPSHOT")
    compileOnly(dependencyNotation = "com.sk89q.worldguard:worldguard-bukkit:7.0.0")
    compileOnly("me.clip:placeholderapi:2.10.9")
    compileOnly(kotlin("stdlib"))
    compileOnly(fileTree("libs"))
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = listOf("-Xjvm-default=all")
    }
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}
