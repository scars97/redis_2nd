package com.example.common

import io.github.bucket4j.Bucket
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.Duration
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

class Bucket4jRateLimiterTest {

    private val cache = ConcurrentHashMap<String, Bucket>()

    @DisplayName("특정 값마다 요청이 제한된다.")
    @Test
    fun whenRequestSpecificValueThenRestricting() {
        // given
        val bucket1 = resolveBucket("TEST-IP-1")
        val bucket2 = resolveBucket("TEST-IP-2")

        // when
        val bucket1IsConsumed = CompletableFuture.supplyAsync { bucket1.tryConsume(10) }
        val bucket2IsConsumed = CompletableFuture.supplyAsync { bucket2.tryConsume(10) }

        // then
        assertThat(bucket1IsConsumed.get()).isTrue()
        assertThat(bucket2IsConsumed.get()).isTrue()
    }

    private fun resolveBucket(value: String): Bucket {
        return cache.computeIfAbsent(value) {
            Bucket.builder()
                .addLimit { limit ->
                    limit.capacity(10)
                        .refillGreedy(10, Duration.ofSeconds(5))
                }
                .build()
        }
    }

    @DisplayName("동시에 55번 요청하는 경우, 50번은 성공하고 5번은 실패한다.")
    @Test
    fun when55RequestsAtTheSameTimeThen50SucceedAnd5Failed() {
        // given
        val requestLimit = 50L
        val bucket = Bucket.builder()
            .addLimit { limit ->
                limit.capacity(requestLimit)
                    .refillIntervally(requestLimit, Duration.ofSeconds(5))
            }
            .build()

        val successCount = AtomicInteger(0)
        val failureCount = AtomicInteger(0)

        // when
        val futures = (1..55).map {
            CompletableFuture.runAsync {
                try {
                    if (bucket.tryConsume(1)) {
                        successCount.incrementAndGet()
                    } else {
                        failureCount.incrementAndGet()
                    }
                } catch (e: Exception) {
                    failureCount.incrementAndGet()
                }
            }
        }

        CompletableFuture.allOf(*futures.toTypedArray()).join()

        // then
        assertThat(successCount.get()).isEqualTo(requestLimit)
        assertThat(failureCount.get()).isEqualTo(5)
    }

}