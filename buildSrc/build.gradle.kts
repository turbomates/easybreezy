group = "io.easybreezy"
version = "1.0-SNAPSHOT"

apply(plugin = "com.github.johnrengelman.shadow")
apply(plugin = "kotlin")

buildscript {
    repositories {
        maven(url = "https://plugins.gradle.org/m2/")
    }

    dependencies {
        classpath("com.github.jengelman.gradle.plugins:shadow:4.0.3")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.50")
    }
}

plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    java
}

repositories {
    jcenter()
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.3.40")
    implementation("com.github.jengelman.gradle.plugins:shadow:4.0.3")
    implementation("org.jdom:jdom2:2.0.5")
    implementation("commons-io:commons-io:2.6")
}


gradlePlugin {
    plugins {
        register("shadow-plugin") {
            id = "io.easybreezy.shadow"
            implementationClass = "shadow.ShadowJarPlugin"
        }
    }
}
