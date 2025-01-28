plugins {
    kotlin("plugin.jpa") version "1.9.22"
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation(project(":common"))
    compileOnly(project(":domains:review"))
    runtimeOnly("org.postgresql:postgresql")
}
