package com.example.infrastructure.config

import jakarta.annotation.PreDestroy
import org.springframework.context.annotation.Configuration
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.utility.DockerImageName

@Configuration
class TestContainerConfig {
    @PreDestroy
    fun preDestroy() {
        if (MYSQL_CONTAINER!!.isRunning()) {
            MYSQL_CONTAINER!!.stop()
        }
    }

    companion object {
        var MYSQL_CONTAINER: MySQLContainer<*>? = null

        init {
            // mysql
            MYSQL_CONTAINER = MySQLContainer(DockerImageName.parse("mysql:8.0"))
                .apply {
                    withDatabaseName("movie-test")
                    withUsername("movie")
                    withPassword("movie")
                }
            MYSQL_CONTAINER!!.start()
            System.setProperty("spring.datasource.url", MYSQL_CONTAINER!!.getJdbcUrl() + "?characterEncoding=UTF-8&serverTimezone=UTC")
            System.setProperty("spring.datasource.username", MYSQL_CONTAINER!!.getUsername())
            System.setProperty("spring.datasource.password", MYSQL_CONTAINER!!.getPassword())
        }
    }
}
