package com.umc.catchandroid.domain.repository

import com.umc.catchandroid.domain.model.Notice

interface CalendarRepository {
    suspend fun getDeadlineDates(year: Int, month: Int): List<String>
    suspend fun getNoticesByDate(date: String): List<Notice>
}