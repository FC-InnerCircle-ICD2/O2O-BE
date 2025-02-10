plugins {
}

tasks.getByName("bootJar") {
    enabled = true
}

tasks.named<Test>("test").configure {
    val envFile = File(rootProject.projectDir, "application-client/.env")

    if (envFile.exists()) {
        envFile.readLines()
            .filter { it.isNotEmpty() && !it.startsWith("#") }
            .forEach { line ->
                val (key, value) = line.split("=", limit = 2)
                environment(key, value)
            }
    }
}

dependencies {
    implementation(project(":domains:member"))
    implementation(project(":domains:store"))
    implementation(project(":domains:order"))
    implementation(project(":domains:payment"))
    implementation(project(":domains:review"))
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    runtimeOnly(project(":infrastructure:member-postgres"))
    runtimeOnly(project(":infrastructure:member-redis"))
    runtimeOnly(project(":infrastructure:order-postgres"))
    runtimeOnly(project(":infrastructure:order-redis"))
    runtimeOnly(project(":infrastructure:payment-postgres"))
    runtimeOnly(project(":infrastructure:store-mongo"))
    runtimeOnly(project(":infrastructure:store-redis"))
    runtimeOnly(project(":infrastructure:review-postgres"))
    implementation(project(":common"))
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.0.2")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("io.micrometer:micrometer-registry-prometheus")
    implementation("org.springframework:spring-tx")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("io.awspring.cloud:spring-cloud-aws-starter-s3:3.1.1")
    implementation("com.auth0:java-jwt:4.2.1")
}
