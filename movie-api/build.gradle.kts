import org.springframework.boot.gradle.tasks.bundling.BootJar

dependencies {
    implementation(project(":movie-application"))
    implementation(project(":movie-infrastructure"))

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
}

tasks.withType<BootJar> {
    enabled = true
}