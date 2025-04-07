dependencies {
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-cache")

    // RateLimiter
    implementation("com.bucket4j:bucket4j_jdk17-core:8.14.0")

    // QueryDsl
    implementation ("com.querydsl:querydsl-jpa:5.0.0:jakarta")
    kapt("com.querydsl:querydsl-apt:5.0.0:jakarta")

    // Redis
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation ("org.redisson:redisson-spring-boot-starter:3.43.0")
}