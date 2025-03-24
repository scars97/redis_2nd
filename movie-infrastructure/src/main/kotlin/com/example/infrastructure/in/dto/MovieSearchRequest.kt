package com.example.infrastructure.`in`.dto

import jakarta.validation.constraints.Size

data class MovieSearchRequest(
    @field:Size(min = 3, max = 100, message = "제목은 3자 이상 100자 이하로 입력해야 합니다.")
    val title: String?,
    @field:Size(min = 1, max = 100, message = "장르은 1자 이상 100자 이하로 입력해야 합니다.")
    val genre:String?
)