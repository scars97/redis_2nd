package com.example.application.validator

import com.example.application.dto.ReservationInfo
import com.example.business.theater.service.TheaterScheduleService
import com.example.business.user.service.UserService
import com.example.common.exception.BusinessException
import com.example.common.exception.ErrorCode.*
import org.springframework.stereotype.Component

@Component
class ReservationValidator(
    private val userService: UserService,
    private val scheduleService: TheaterScheduleService
) {

    fun validate(info: ReservationInfo) {
        if (!userService.isUserExists(info.userId)) {
            throw BusinessException(USER_NOT_FOUND, "존재하지 않는 회원 ID: ${info.userId}")
        }

        if (!scheduleService.isScheduleExists(info.scheduleId)) {
            throw BusinessException(SCHEDULE_NOT_FOUND, "존재하지 않는 상영 일정 ID: ${info.scheduleId}")
        }
    }

}