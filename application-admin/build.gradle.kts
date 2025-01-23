plugins {
}

tasks.getByName("bootJar") {
    enabled = true
}

dependencies {
    implementation(project(":domains:store"))
    implementation(project(":domains:order"))
    implementation(project(":common"))
    runtimeOnly(project(":infrastructure:order-postgres"))
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.0.2")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework:spring-tx")
}
