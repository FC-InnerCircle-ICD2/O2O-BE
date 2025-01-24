plugins {
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation(project(":domains:order"))
}
