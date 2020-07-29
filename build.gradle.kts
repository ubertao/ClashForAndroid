// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    val gKotlinVersion: String by project
    val gRetrofixPluginVersion: String by project

    repositories {
        google()
        jcenter()
        gradlePluginPortal()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:4.0.0")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$gKotlinVersion")
        classpath("org.jetbrains.kotlin:kotlin-serialization:$gKotlinVersion")
        classpath("gradle.plugin.com.github.sgtsilvio.gradle:android-retrofix:$gRetrofixPluginVersion")
    }
}

allprojects {
    repositories {
        google()
        jcenter()

        maven {
            url = java.net.URI("https://dl.bintray.com/rikkaw/Libraries")
        }
        maven {
            url = java.net.URI("https://dl.bintray.com/rikkaw/MaterialPreference")
        }
    }
}

task("clean", type = Delete::class) {
    delete(rootProject.buildDir)
}
