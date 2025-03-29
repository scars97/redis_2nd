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

    runtimeOnly("com.mysql:mysql-connector-j")
}