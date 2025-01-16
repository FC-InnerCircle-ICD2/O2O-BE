plugins {
}

dependencies {
    implementation(project(":domains:member"))
    implementation(project(":domains:store"))
    implementation(project(":domains:order"))
    implementation(project(":infrastructure:store-mongo"))
    implementation(project(":infrastructure:store-redis"))
    implementation(project(":infrastructure:order-postgres"))
    implementation(project(":common"))
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework:spring-tx")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.0.2")
}
