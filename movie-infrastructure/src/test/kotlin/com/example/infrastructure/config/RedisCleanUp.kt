package com.example.infrastructure.config

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component

@Component
class RedisCleanUp(
    private val redisTemplate: RedisTemplate<String, Any>
) {

    fun execute() {
        val keys = redisTemplate.keys("*")
        if (keys.isNotEmpty()) {
            redisTemplate.delete(keys)
        }
    }

}