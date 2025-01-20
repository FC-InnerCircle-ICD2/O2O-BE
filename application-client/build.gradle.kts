plugins {
}

dependencies {
    implementation(project(":domains:member"))
    implementation(project(":domains:store"))
    implementation(project(":domains:order"))
    implementation(project(":domains:payment"))
    runtimeOnly(project(":infrastructure:store-mongo"))
    runtimeOnly(project(":infrastructure:store-redis"))
    runtimeOnly(project(":infrastructure:order-postgres"))
    runtimeOnly(project(":infrastructure:payment-postgres"))
    implementation(project(":common"))
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework:spring-tx")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.0.2")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
}
