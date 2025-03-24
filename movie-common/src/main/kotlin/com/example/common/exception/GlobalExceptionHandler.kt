package com.example.common.exception

import com.example.common.model.BindErrorResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.validation.BindException

@RestControllerAdvice
class GlobalExceptionHandler {

    private val log = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

    @ExceptionHandler(BindException::class)
    fun validationExceptionHandler(e: BindException): ResponseEntity<BindErrorResponse> {
        val errors = e.fieldErrors.associate { it.field to it.defaultMessage.orEmpty() }

        errors.forEach { (field, message) ->
            log.error("Invalid Input: {} - {}", field, message)
        }

        return BindErrorResponse.of(HttpStatus.BAD_REQUEST, errors)
    }

}