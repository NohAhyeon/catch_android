package com.umc.catchandroid.data.repository

import com.umc.catchandroid.domain.model.Notice
import com.umc.catchandroid.domain.repository.CalendarRepository
import kotlinx.coroutines.delay
import javax.inject.Inject

class MockCalendarRepositoryImpl @Inject constructor() : CalendarRepository {

    private val noticesByDate = mapOf(
        "2026-06-20" to listOf(
            Notice(
                noticeId = 30,
                categoryTag = "비교과",
                title = "글로벌 역량강화 프로그램 마감",
                source = "국제교류처",
                createdAt = "2026-06-01T09:00:00",
                deadlineAt = "2026-06-20T23:59:59"
            ),
            Notice(
                noticeId = 31,
                categoryTag = "장학",
                title = "2026학년도 2학기 국가장학금 2차 신청",
                source = "학생복지처",
                createdAt = "2026-06-01T09:00:00",
                deadlineAt = "2026-06-20T18:00:00"
            )
        ),
        "2026-06-25" to listOf(
            Notice(
                noticeId = 32,
                categoryTag = "학사",
                title = "수강 정정 기간 종료",
                source = "교무처",
                createdAt = "2026-06-01T09:00:00",
                deadlineAt = "2026-06-25T18:00:00"
            )
        )
    )

    private val upcomingNotices = listOf(
        Notice(
            noticeId = 32,
            categoryTag = "학사",
            title = "수강 정정 기간 종료",
            source = "교무처",
            createdAt = "2026-06-01T09:00:00",
            deadlineAt = "2026-06-25T18:00:00"
        ),
        Notice(
            noticeId = 33,
            categoryTag = "취업",
            title = "교내 채용설명회 사전 신청",
            source = "취업지원팀",
            createdAt = "2026-06-01T09:00:00",
            deadlineAt = "2026-06-28T18:00:00"
        ),
        Notice(
            noticeId = 34,
            categoryTag = "대외활동",
            title = "여름방학 봉사활동 신청 마감",
            source = "학생지원팀",
            createdAt = "2026-06-01T09:00:00",
            deadlineAt = "2026-06-30T18:00:00"
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

    override suspend fun getUpcomingNotices(): List<Notice> {
        delay(200)
        return upcomingNotices
    }
}