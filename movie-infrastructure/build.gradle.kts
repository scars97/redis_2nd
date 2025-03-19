dependencies {
    implementation(project(":movie-application"))
    implementation(project(":movie-business"))

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly ("org.junit.platform:junit-platform-launcher")

    runtimeOnly("com.mysql:mysql-connector-j")
}