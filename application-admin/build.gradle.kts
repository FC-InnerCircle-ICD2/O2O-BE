plugins {
}

tasks.getByName("bootJar") {
    enabled = true
}

dependencies {
    implementation(project(":domains:member"))
    implementation(project(":domains:order"))
    runtimeOnly(project(":infrastructure:member-postgres"))
    runtimeOnly(project(":infrastructure:order-postgres"))
    runtimeOnly(project(":infrastructure:order-redis"))
    implementation(project(":common"))
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework:spring-tx")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.0.2")
}
