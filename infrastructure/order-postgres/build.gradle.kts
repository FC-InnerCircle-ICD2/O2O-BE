plugins {
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation(project(":common"))
    implementation(project(":domains:order"))
    runtimeOnly("org.postgresql:postgresql")
}
