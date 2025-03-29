package com.example.application.validator

import com.example.application.dto.ReservationInfo
import com.example.business.theater.service.TheaterScheduleService
import com.example.business.user.service.UserService
import org.springframework.stereotype.Component
import java.lang.IllegalArgumentException

@Component
class ReservationValidator(
    private val userService: UserService,
    private val scheduleService: TheaterScheduleService
) {

    fun validate(info: ReservationInfo) {
        if (!userService.isUserExists(info.userId)) {
            throw IllegalArgumentException("존재하지 않는 회원 ID: ${info.userId}")
        }

        if (!scheduleService.isScheduleExists(info.scheduleId)) {
            throw IllegalArgumentException("존재하지 않는 상영 일정 ID: ${info.scheduleId}")
        }
    }

}