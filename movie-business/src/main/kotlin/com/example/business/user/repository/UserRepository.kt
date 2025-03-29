package com.example.business.user.repository

interface UserRepository {

    fun existsBy(userId: Long): Boolean

}