plugins {
    kotlin("plugin.jpa") version "1.9.21"
}

dependencies {
    implementation(project(":infrastructure:store-redis"))
    implementation(project(":infrastructure:store-mongo"))
    implementation(project(":common"))
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
}
