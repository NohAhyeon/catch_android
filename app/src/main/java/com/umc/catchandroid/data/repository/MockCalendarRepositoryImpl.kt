package com.umc.catchandroid.data.repository

import com.umc.catchandroid.domain.model.Notice
import com.umc.catchandroid.domain.repository.CalendarRepository
import kotlinx.coroutines.delay
import javax.inject.Inject

class MockCalendarRepositoryImpl @Inject constructor() : CalendarRepository {

    private val noticesByDate = mapOf(
        "2026-07-15" to listOf(
            Notice(
                noticeId = 20,
                categoryTag = "장학",
                title = "2026학년도 2학기 교내 성적우수 장학금 신청",
                source = "학생복지처",
                createdAt = "2026-07-01T09:00:00",
                deadlineAt = "2026-07-15T18:00:00"
            )
        ),
        "2026-07-20" to listOf(
            Notice(
                noticeId = 21,
                categoryTag = "학사",
                title = "2026학년도 2학기 전과 신청 및 선발 일정 안내",
                source = "교무처",
                createdAt = "2026-07-02T14:00:00",
                deadlineAt = "2026-07-20T23:59:59"
            )
        ),
        "2026-07-25" to listOf(
            Notice(
                noticeId = 22,
                categoryTag = "취업",
                title = "교내 채용설명회 사전 신청",
                source = "취업지원팀",
                createdAt = "2026-07-03T11:00:00",
                deadlineAt = "2026-07-25T18:00:00"
            )
        )
    )

    override suspend fun getDeadlineDates(year: Int, month: Int): List<String> {
        delay(200)
        return noticesByDate.keys.toList()
    }

    override suspend fun getNoticesByDate(date: String): List<Notice> {
        delay(200)
        return noticesByDate[date] ?: emptyList()
    }
}