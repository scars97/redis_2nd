package com.example.infrastructure.out.external.message

import com.example.application.event.ReservationMessageEvent
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener
import java.io.IOException

@Component
class MessageService {

    private val log = LoggerFactory.getLogger(MessageService::class.java)

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun sendMessage(messageEvent: ReservationMessageEvent) {
        try {
            log.info("App push 발송 : ${messageEvent.reservationId}")
        } catch (e: IOException) {
            e.message
        }
    }

}