package com.example.common.lock

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Component
class FunctionalForTransaction {

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun <T> proceed(action: () -> T): T {
        return action()
    }

}
