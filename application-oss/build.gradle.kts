plugins {
}

tasks.getByName("bootJar") {
    enabled = true
}
dependencies {
    implementation(project(":domains:member"))
    implementation(project(":domains:order"))
    implementation(project(":domains:payment"))
    implementation(project(":domains:store"))
    runtimeOnly(project(":infrastructure:member-postgres"))
    runtimeOnly(project(":infrastructure:order-postgres"))
    runtimeOnly(project(":infrastructure:payment-postgres"))
    runtimeOnly(project(":infrastructure:store-mongo"))
    runtimeOnly(project(":infrastructure:external-pg-toss-payments"))
    runtimeOnly(project(":infrastructure:external-pg-pay200"))
    implementation(project(":common"))
    implementation("org.springframework.boot:spring-boot-starter-batch")
    implementation("org.springframework.boot:spring-boot-starter-quartz")
    implementation("org.springframework:spring-tx")
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:2023.0.0")
    }
}
