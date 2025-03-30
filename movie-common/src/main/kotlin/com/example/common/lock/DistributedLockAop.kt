package com.example.common.lock

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.redisson.api.RedissonClient
import org.slf4j.LoggerFactory
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

@Aspect
@Order(Ordered.HIGHEST_PRECEDENCE)
@Component
class DistributedLockAop(
    private val redissonClient: RedissonClient
) {

    private val log = LoggerFactory.getLogger(DistributedLockAop::class.java)

    companion object {
        const val REDISSON_LOCK_PREFIX = "LOCK:"
    }

    @Around("@annotation(com.example.common.lock.DistributedLock)")
    @Throws(Throwable::class)
    fun lock(joinPoint: ProceedingJoinPoint): Any {
        val signature = joinPoint.signature as MethodSignature
        val method = signature.method
        val distributedLock = method.getAnnotation(DistributedLock::class.java)

        val key: String = REDISSON_LOCK_PREFIX + CustomSpringELParser.getDynamicValue(
            signature.parameterNames,
            joinPoint.args,
            distributedLock.key
        )
        val rLock = redissonClient.getLock(key) // 락 이름으로 RLock 인스턴스를 가져온다.

        return try {
            // 정의된 waitTime 획득 시도. 정의된 leaseTime이 지나면 잠금을 해제한다.
            val available = rLock.tryLock(
                distributedLock.waitTime,
                distributedLock.leaseTime,
                distributedLock.timeUnit
            )

            if (!available) {
                false
            } else {
                log.info("분산락 적용됨")
                joinPoint.proceed()
            }
        } catch (e: InterruptedException) {
            throw InterruptedException()
        } finally {
            try {
                rLock.unlock() // 종료 시 무조건 락을 해제.
            } catch (e: IllegalMonitorStateException) {
                log.info("Redisson Lock Already UnLock {} {}", method.name, key)
            }
        }
    }

}