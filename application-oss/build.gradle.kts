plugins {
}

tasks.getByName("bootJar") {
    enabled = true
}
dependencies {
    implementation(project(":domains:order"))
    implementation(project(":domains:payment"))
    runtimeOnly(project(":infrastructure:order-postgres"))
    runtimeOnly(project(":infrastructure:payment-postgres"))
    implementation(project(":common"))
    implementation("org.springframework.boot:spring-boot-starter-batch")
    implementation("org.springframework.boot:spring-boot-starter-quartz")
    implementation("org.springframework:spring-tx")
}
