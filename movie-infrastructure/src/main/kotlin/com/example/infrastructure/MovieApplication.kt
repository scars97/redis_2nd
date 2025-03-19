package com.example.infrastructure

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = [
    "com.example.application",
    "com.example.business",
    "com.example.infrastructure"
])
class MovieApplication

fun main(args: Array<String>) {
    runApplication<MovieApplication>(*args)
}