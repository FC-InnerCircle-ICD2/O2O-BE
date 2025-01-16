plugins {
}

dependencies {
    implementation(project(":domains:store"))
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework:spring-tx")
}
