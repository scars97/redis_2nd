dependencies {
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-cache")

    // QueryDsl
    implementation ("com.querydsl:querydsl-jpa:5.0.0:jakarta")
    kapt("com.querydsl:querydsl-apt:5.0.0:jakarta")

    // Redis
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation ("org.redisson:redisson-spring-boot-starter:3.43.0")
}