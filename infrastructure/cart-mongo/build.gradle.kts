plugins {
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    compileOnly(project(":domains:cart"))
    implementation(project(":common"))
}
