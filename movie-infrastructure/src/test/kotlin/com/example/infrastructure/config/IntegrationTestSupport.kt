package com.example.infrastructure.config

import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("test")
@SpringBootTest
class IntegrationTestSupport {

    @Autowired
    private lateinit var databaseCleanUp: DatabaseCleanUp

    @BeforeEach
    fun execute() {
        databaseCleanUp.execute()
    }

}