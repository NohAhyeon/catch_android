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
            title = "국가장학금 신청 안내",
            source = "학생복지처",
            createdAt = "2026-07-13T04:00:00",
            deadlineAt = "2026-07-22T18:00:00"
        ),
        Notice(
            noticeId = 11,
            categoryTag = "교내",
            title = "교내 장학금 추가 모집",
            source = "학생복지처",
            createdAt = "2026-07-12T09:00:00",
            deadlineAt = "2026-08-02T18:00:00"
        ),
        Notice(
            noticeId = 12,
            categoryTag = "교환학생",
            title = "2026-1학기 교환학생 모집 안내",
            source = "국제교류처",
            createdAt = "2026-07-12T09:00:00",
            deadlineAt = "2026-07-28T18:00:00"
        ),
        Notice(
            noticeId = 13,
            categoryTag = "비교과",
            title = "글로벌 역량강화 프로그램 모집",
            source = "국제교육처",
            createdAt = "2026-07-11T09:00:00",
            deadlineAt = "2026-07-20T18:00:00"
        ),
        Notice(
            noticeId = 14,
            categoryTag = "취업",
            title = "교내 채용설명회 사전 신청",
            source = "취업지원팀",
            createdAt = "2026-07-03T09:00:00",
            deadlineAt = "2026-07-23T18:00:00"
        )
    )

    override suspend fun searchNotices(keyword: String): List<Notice> {
        delay(300)
        if (keyword.isBlank()) return allNotices
        return allNotices.filter { it.title.contains(keyword) || it.categoryTag.contains(keyword) }
    }
}