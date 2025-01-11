plugins {

}

dependencies {
    implementation(project(":infrastructure:store-redis"))
    implementation(project(":infrastructure:store-mongo"))
    implementation(project(":common"))
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
}
