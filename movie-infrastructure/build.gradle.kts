dependencies {
    implementation(project(":movie-application"))
    implementation(project(":movie-business"))
    implementation(project(":movie-common"))

    implementation("org.springframework.boot:spring-boot-starter-validation")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly ("org.junit.platform:junit-platform-launcher")

    // QueryDsl
    implementation ("com.querydsl:querydsl-jpa:5.0.0:jakarta")
    kapt("com.querydsl:querydsl-apt:5.0.0:jakarta")

    // Mysql
    runtimeOnly("com.mysql:mysql-connector-j")

    // Redis
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation ("org.redisson:redisson-spring-boot-starter:3.43.0")

    // TestContainer
    testImplementation ("org.testcontainers:mysql")
    testImplementation ("com.redis:testcontainers-redis")
}