plugins {

}

dependencies {
    implementation(project(":common"))
    implementation(project(":infrastructure:order-postgres"))
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
}
