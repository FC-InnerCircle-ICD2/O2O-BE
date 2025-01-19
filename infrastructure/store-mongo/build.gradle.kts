plugins {
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    compileOnly(project(":domains:store"))
    implementation(project(":common"))
}
