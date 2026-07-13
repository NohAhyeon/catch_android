package com.umc.catchandroid.data.repository

import com.umc.catchandroid.domain.model.Notice
import com.umc.catchandroid.domain.repository.SearchRepository
import kotlinx.coroutines.delay
import javax.inject.Inject

class MockSearchRepositoryImpl @Inject constructor() : SearchRepository {

    private val allNotices = listOf(
        Notice(
            noticeId = 10,
            categoryTag = "장학",
            title = "2026학년도 2학기 국가장학금 신청 안내",
            source = "학생복지처",
            createdAt = "2026-07-01T09:00:00",
            deadlineAt = "2026-07-10T23:59:59"
        ),
        Notice(
            noticeId = 11,
            categoryTag = "취업",
            title = "교내 창업경진대회 참가자 모집",
            source = "창업지원단",
            createdAt = "2026-07-02T10:00:00",
            deadlineAt = "2026-07-20T18:00:00"
        ),
        Notice(
            noticeId = 12,
            categoryTag = "학사",
            title = "2026-1학기 성적 이의신청 안내",
            source = "교무처",
            createdAt = "2026-06-20T09:00:00",
            deadlineAt = null
        )
    )

    override suspend fun searchNotices(keyword: String): List<Notice> {
        delay(300)
        if (keyword.isBlank()) return emptyList()
        return allNotices.filter { it.title.contains(keyword) }
    }
}