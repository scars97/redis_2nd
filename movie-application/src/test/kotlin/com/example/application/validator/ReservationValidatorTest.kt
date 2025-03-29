package com.example.application.validator

import com.example.application.dto.ReservationInfo
import com.example.business.theater.service.TheaterScheduleService
import com.example.business.user.service.UserService
import com.example.common.exception.BusinessException
import com.example.common.exception.ErrorCode
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class ReservationValidatorTest {

    @InjectMocks
    private lateinit var sut: ReservationValidator

    @Mock
    private lateinit var userService: UserService

    @Mock
    private lateinit var scheduleService: TheaterScheduleService

    @BeforeEach
    fun setUp() {
        sut = ReservationValidator(userService, scheduleService)
    }

    @DisplayName("회원ID, 상영일정ID 중 하나라도 존재하지 않으면 예외가 발생한다.")
    @Test
    fun reservationValidate() {
        // given
        val info = ReservationInfo(1L, 1L, listOf(1L,2L,3L))
        `when`(userService.isUserExists(info.userId)).thenReturn(true)
        `when`(scheduleService.isScheduleExists(info.scheduleId)).thenReturn(false)

        // when // then
        assertThatThrownBy { sut.validate(info) }
            .isInstanceOf(BusinessException::class.java)
            .hasFieldOrPropertyWithValue("errorCode", ErrorCode.SCHEDULE_NOT_FOUND)
            .hasMessage("존재하지 않는 상영 일정 ID: ${info.scheduleId}")
    }

}