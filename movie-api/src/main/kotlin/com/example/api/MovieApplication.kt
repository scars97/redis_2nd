package com.example.api

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@EntityScan(basePackages = ["com.example.infrastructure"])
@EnableJpaRepositories(basePackages = ["com.example.infrastructure"])
@SpringBootApplication(scanBasePackages = [
    "com.example.application",
    "com.example.api",
    "com.example.business",
    "com.example.infrastructure"
])
class MovieApplication

fun main(args: Array<String>) {
    runApplication<MovieApplication>(*args)
}