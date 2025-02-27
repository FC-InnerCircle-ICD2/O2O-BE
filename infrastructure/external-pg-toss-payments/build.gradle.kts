plugins {
}

dependencies {
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation(project(":common"))

    compileOnly(project(":domains:payment"))
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:2023.0.0")
    }
}
