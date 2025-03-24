package com.example.common.model

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

data class BindErrorResponse(
    val status: HttpStatus,
    val reason: Map<String, String> = mapOf()
) {
    companion object {
        fun of(
            status: HttpStatus, reason: Map<String, String>
        ): ResponseEntity<BindErrorResponse> {
            return ResponseEntity.status(status).body(BindErrorResponse(status, reason))
        }
    }
}
