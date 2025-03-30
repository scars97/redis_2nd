package com.example.common.lock

import org.redisson.api.RedissonClient
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class DistributedLockExecutor(
    private val redissonClient: RedissonClient,
    private val functionalForTransaction: FunctionalForTransaction
){

    private val log = LoggerFactory.getLogger(DistributedLockExecutor::class.java)

    companion object {
        const val REDISSON_LOCK_PREFIX = "LOCK:"
    }

    fun <T> lockWithTransaction(key: String, waitTime: Long, leaseTime: Long, action: () -> T): T {
        val rLock = redissonClient.getLock(REDISSON_LOCK_PREFIX + key)

        return try {
            if (!rLock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS)) {
                throw RuntimeException("락 획득 실패 : $key")
            }

            functionalForTransaction.proceed(action)
        } catch (e: InterruptedException) {
            throw InterruptedException()
        } finally {
            try {
                if (rLock.isLocked && rLock.isHeldByCurrentThread) {
                    rLock.unlock()
                }
            } catch (e: IllegalMonitorStateException) {
                log.info("Redisson Lock Already UnLock : $key")
            }
        }
    }

}