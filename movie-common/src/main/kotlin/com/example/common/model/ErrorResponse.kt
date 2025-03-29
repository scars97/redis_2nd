package com.example.common.model

import com.example.common.exception.ErrorCode
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

data class ErrorResponse(
    val status: HttpStatus,
    val message: String?
) {

    companion object {
        fun of(errorCode: ErrorCode, message: String?): ResponseEntity<ErrorResponse> {
            return ResponseEntity
                .status(errorCode.status)
                .body(ErrorResponse(errorCode.status, message))
        }
    }

}
