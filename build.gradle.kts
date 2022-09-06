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

import java.time.Duration
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

plugins {
    `java-library`
    `maven-publish`
    signing

    id("com.gorylenko.gradle-git-properties") version("2.4.1")
    id("com.diffplug.spotless") version("6.8.0")
    id("io.github.gradle-nexus.publish-plugin") version("1.1.0")
}

group = "com.jcovalent.junit"

repositories {
    mavenCentral()
}

java {
    withJavadocJar()
    withSourcesJar()
}

dependencies {
    implementation(libs.bundles.junit.platform.api)
    implementation(libs.bundles.junit.jupiter.api)
    implementation(libs.bundles.slf4j.api)
    implementation(libs.bundles.log4j.api)
    implementation(libs.bundles.logback.api)
    implementation(libs.bundles.logback.runtime)
    implementation(testLibs.bundles.assertj)

    runtimeOnly(libs.bundles.log4j.runtime)

    testImplementation(libs.bundles.log4j.runtime)
}

gitProperties {
    keys = listOf("git.commit.id", "git.commit.id.abbrev", "git.build.version")
}

tasks.withType<AbstractArchiveTask>() {
    isPreserveFileTimestamps = false
    isReproducibleFileOrder = true
    dirMode = 775
    fileMode = 664
}

spotless {
    ratchetFrom("origin/main")
    format("misc") {
        // define the files to apply `misc` to
        target("*.gradle", "*.md", ".gitignore")

        // define the steps to apply to those files
        trimTrailingWhitespace()
        indentWithSpaces()
        endWithNewline()
    }

    format("actionYaml") {
        target(".github/workflows/*.yaml")
        prettier()

        licenseHeader(
            """
            ##
            # Copyright (C) ${'$'}YEAR JCovalent
            #
            # Licensed under the Apache License, Version 2.0 (the "License");
            # you may not use this file except in compliance with the License.
            # You may obtain a copy of the License at
            #
            #      http://www.apache.org/licenses/LICENSE-2.0
            #
            # Unless required by applicable law or agreed to in writing, software
            # distributed under the License is distributed on an "AS IS" BASIS,
            # WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
            # See the License for the specific language governing permissions and
            # limitations under the License.
            ##
            """.trimIndent(),
            "(name)"
        )
    }

    java {
        targetExclude("build/generated/sources/**/*.java")
        // enable toggle comment support
        toggleOffOn()
        // don't need to set target, it is inferred from java
        // apply a specific flavor of google-java-format
        googleJavaFormat().aosp().reflowLongStrings()
        // make sure every file has the following copyright header.
        // optionally, Spotless can set copyright years by digging
        // through git history (see "license" section below).
        // The delimiter override below is required to support some
        // of our test classes which are in the default package.
        licenseHeader(
            """
           /*
            * Copyright (C) ${'$'}YEAR JCovalent
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
            """.trimIndent(),
            "(package|import|module)"
        )
    }

    kotlinGradle {
        ktlint().editorConfigOverride(mapOf("disabled_rules" to "no-wildcard-imports"))

        licenseHeader(
            """
           /*
            * Copyright (C) ${'$'}YEAR JCovalent
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
            */${"\n\n"}
            """.trimIndent(),
            "(import|plugins|pluginManagement|dependencies)"
        )
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components.getByName("java"))

            pom {
                packaging = "jar"
                name.set(project.name)
                url.set("https://www.jcovalent.com")
                inceptionYear.set("2022")

                description.set("${project.name} provides SLF4J and Log4j2 extensions for JUnit5 tests.")

                organization {
                    name.set("JCovalent")
                    url.set("https://www.jcovalent.com")
                }

                licenses {
                    license {
                        name.set("Apache 2.0 License")
                        url.set("https://raw.githubusercontent.com/hashgraph/swirlds-open-review/main/LICENSE")
                    }
                }

                developers {
                    developer {
                        name.set("Nathan Klick")
                        email.set("nathan@jcovalent.com")
                        organization.set("JCovalent")
                        organizationUrl.set("https://www.jcovalent.com")
                    }
                }

                scm {
                    url.set("https://github.com/jcovalent/jcovalent-junit-logging")
                    connection.set("scm:git:git://github.com/jcovalent/jcovalent-junit-logging.git")
                    developerConnection.set("scm:git:ssh://github.com:jcovalent/jcovalent-junit-logging.git")
                }
            }
        }
        repositories {
            maven {
                name = "github"
                url = uri("https://maven.pkg.github.com/jcovalent/jcovalent-junit-logging")
                credentials {
                    username = System.getenv("GITHUB_ACTOR")
                    password = System.getenv("GITHUB_TOKEN")
                }
            }
        }
    }
}

nexusPublishing {
    useStaging.set(true)
    transitionCheckOptions {
        delayBetween.set(Duration.ofSeconds(20))
    }

    repositories {
        sonatype { // only for users registered in Sonatype after 24 Feb 2021
            nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
            snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))
            username.set(System.getenv("MAVEN_CENTRAL_USERNAME"))
            password.set(System.getenv("MAVEN_CENTRAL_PASSWORD"))
        }
    }
}

signing {
    useGpgCmd()
    sign(publishing.publications.getByName("maven"))
}
