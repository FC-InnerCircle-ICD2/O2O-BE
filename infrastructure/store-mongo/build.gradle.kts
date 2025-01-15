plugins {
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    implementation(project(":domains:store"))
    implementation(project(":common"))
}
