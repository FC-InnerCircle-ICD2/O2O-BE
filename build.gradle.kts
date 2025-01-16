@file:Suppress("LABEL_NAME_CLASH")

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.2.1" apply false
    id("io.spring.dependency-management") version "1.1.4" apply false
    id("org.jlleitschuh.gradle.ktlint") version "12.0.3"
    id("jacoco")
    kotlin("jvm") version "1.9.21"
    kotlin("plugin.spring") version "1.9.21" apply false
}

val projectGroup: String by project
val applicationVersion: String by project

java {
    sourceCompatibility = JavaVersion.VERSION_21
}

tasks.withType<JavaCompile> {
    options.release.set(21) // compileJava 타겟 설정
}

allprojects {
    group = "org.fastcampus"
    version = "1.0-SNAPSHOT"

    repositories {
        mavenCentral()
    }

    apply {
        plugin("org.jlleitschuh.gradle.ktlint")
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs += "-Xjsr305=strict"
            jvmTarget = JavaVersion.VERSION_21.toString()
        }
    }

    tasks.register("addGitPreCommitHook", Copy::class) {
        from(file("script/pre-commit"))
        into(file(".git/hooks"))
    }
}

subprojects {
    apply {
        plugin("java")
        plugin("io.spring.dependency-management")
        plugin("org.springframework.boot")
        plugin("org.jetbrains.kotlin.plugin.spring")
        plugin("kotlin")
        plugin("kotlin-spring")
        plugin("jacoco")
    }

    dependencies {
        implementation("org.springframework.boot:spring-boot-starter")
        implementation("org.jetbrains.kotlin:kotlin-reflect")
        testImplementation("org.springframework.boot:spring-boot-starter-test")
        testImplementation("io.strikt:strikt-core:0.34.0")
    }

    tasks.withType<Test> {
        useJUnitPlatform()
        finalizedBy("jacocoTestReport")
    }

    tasks.register<JacocoReport>("jacocoRootReport") {
        subprojects {
            this@subprojects.plugins.withType<JacocoPlugin>().configureEach {
                this@subprojects.tasks.matching {
                    it.extensions.findByType<JacocoTaskExtension>() != null
                }
                    .configureEach {
                        sourceSets(this@subprojects.the<SourceSetContainer>().named("main").get())
                        executionData(this)
                    }
            }
        }

        reports {
            xml.outputLocation.set(File("${rootProject.projectDir}/build/reports/jacoco/jacocoTestReport.xml"))
            xml.required.set(true)
            html.required.set(false)
        }
    }

    tasks.jacocoTestCoverageVerification {
        dependsOn(tasks.jacocoTestReport)

        violationRules {
            rule {
                limit {
                    minimum = "0.60".toBigDecimal()
                }
            }
        }
    }

    tasks.getByName("bootJar") {
        enabled = false
    }

    tasks.getByName("jar") {
        enabled = true
    }
}
