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
    implementation(project(":domains:review"))
    runtimeOnly(project(":infrastructure:member-postgres"))
    runtimeOnly(project(":infrastructure:member-redis"))
    runtimeOnly(project(":infrastructure:store-mongo"))
    runtimeOnly(project(":infrastructure:store-redis"))
    runtimeOnly(project(":infrastructure:order-postgres"))
    runtimeOnly(project(":infrastructure:order-redis"))
    runtimeOnly(project(":infrastructure:payment-postgres"))
    runtimeOnly(project(":infrastructure:review-postgres"))
    implementation(project(":common"))
    implementation("io.github.oshai:kotlin-logging-jvm:7.0.3")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.0.2")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework:spring-tx")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("com.auth0:java-jwt:4.2.1")
    implementation("org.springframework.retry:spring-retry")
    testImplementation("io.mockk:mockk:1.13.3")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.4.0")
}
