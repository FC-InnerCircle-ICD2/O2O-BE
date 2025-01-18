plugins {
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-redis")

    compileOnly(project(":domains:store"))

}
