plugins {
}

tasks.getByName("bootJar") {
    enabled = true
}

dependencies {
    implementation(project(":domains:member"))
    implementation(project(":domains:store"))
    implementation(project(":domains:order"))
    implementation(project(":domains:payment"))
    runtimeOnly(project(":infrastructure:member-postgres"))
    runtimeOnly(project(":infrastructure:store-mongo"))
    runtimeOnly(project(":infrastructure:store-redis"))
    runtimeOnly(project(":infrastructure:order-postgres"))
    runtimeOnly(project(":infrastructure:order-redis"))
    runtimeOnly(project(":infrastructure:payment-postgres"))
    implementation(project(":common"))
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework:spring-tx")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.0.2")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("com.auth0:java-jwt:4.2.1")
}
