/*
 * Copyright (C) 2022 JCovalent
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

plugins {
    id("com.gradle.enterprise") version("3.11.1")
}

dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    versionCatalogs {
        create("libs") {
            version("slf4j-version", "2.0.0")
            version("logback-version", "1.4.0")
            version("log4j-version", "2.18.0")
            version("junit-jupiter-version", "5.9.0")
            version("junit-platform-version", "1.9.0")

            bundle("slf4j-api", listOf("slf4j-api"))
            bundle("logback-api", listOf("logback-core"))
            bundle("logback-runtime", listOf("logback-classic"))
            bundle("log4j-api", listOf("log4j-api"))
            bundle("log4j-runtime", listOf("log4j-bridge"))
            bundle("junit-platform-api", listOf("junit-platform-commons"))
            bundle("junit-jupiter-api", listOf("junit-jupiter-api"))
            bundle("junit-jupiter-engine", listOf("junit-jupiter-engine"))

            library("slf4j-api", "org.slf4j", "slf4j-api").versionRef("slf4j-version")
            library("logback-core", "ch.qos.logback", "logback-core").versionRef("logback-version")
            library("logback-classic", "ch.qos.logback", "logback-classic").versionRef("logback-version")
            library("log4j-api", "org.apache.logging.log4j", "log4j-api").versionRef("log4j-version")
            library("log4j-bridge", "org.apache.logging.log4j", "log4j-to-slf4j").versionRef("log4j-version")

            library("junit-platform-commons", "org.junit.platform", "junit-platform-commons").versionRef("junit-platform-version")

            library("junit-jupiter-api", "org.junit.jupiter", "junit-jupiter-api").versionRef("junit-jupiter-version")
            library("junit-jupiter-params", "org.junit.jupiter", "junit-jupiter-params").versionRef("junit-jupiter-version")
            library("junit-jupiter-engine", "org.junit.jupiter", "junit-jupiter-engine").versionRef("junit-jupiter-version")
        }

        create("testLibs") {
            version("assertj-version", "3.23.1")

            bundle("assertj", listOf("assertj-core"))

            library("assertj-core", "org.assertj", "assertj-core").versionRef("assertj-version")
        }
    }
}

gradleEnterprise {
    buildScan {
        termsOfServiceUrl = "https://gradle.com/terms-of-service"
        termsOfServiceAgree = "yes"
    }
}
