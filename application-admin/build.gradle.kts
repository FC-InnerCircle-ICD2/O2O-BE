plugins {
}

tasks.getByName("bootJar") {
    enabled = true
}

dependencies {
    implementation(project(":domains:member"))
    implementation(project(":domains:store"))
    implementation(project(":common"))
    runtimeOnly(project(":infrastructure:member-postgres"))
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.0.2")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework:spring-tx")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("com.auth0:java-jwt:4.2.1")
}
