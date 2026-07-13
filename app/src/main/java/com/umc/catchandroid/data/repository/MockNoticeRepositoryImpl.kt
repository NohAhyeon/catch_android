package com.umc.catchandroid.data.repository

import com.umc.catchandroid.domain.model.Notice
import com.umc.catchandroid.domain.repository.NoticeRepository
import kotlinx.coroutines.delay
import javax.inject.Inject

class MockNoticeRepositoryImpl @Inject constructor() : NoticeRepository {

    private val dummyNotices = listOf(
        Notice(
            noticeId = 1,
            categoryTag = "학사",
            title = "2026학년도 2학기 수강신청 기간 안내",
            source = "컴퓨터공학과 사무실",
            createdAt = "2026-07-01T09:00:00",
            deadlineAt = "2026-07-09T23:59:59"
        ),
        Notice(
            noticeId = 2,
            categoryTag = "장학",
            title = "이공계 국가우수장학금 추가 선발 안내",
            source = "학생복지처",
            createdAt = "2026-06-25T10:00:00",
            deadlineAt = "2026-07-09T23:59:59"
        ),
        Notice(
            noticeId = 3,
            categoryTag = "비교과",
            title = "글로벌 역량강화 프로그램 모집",
            source = "국제교류처",
            createdAt = "2026-07-02T14:00:00",
            deadlineAt = "2026-07-15T18:00:00"
        ),
        Notice(
            noticeId = 4,
            categoryTag = "취업",
            title = "교내 채용설명회 사전 신청",
            source = "취업지원팀",
            createdAt = "2026-07-03T11:00:00",
            deadlineAt = null
        )
    )

    override suspend fun getNotices(page: Int, size: Int): List<Notice> {
        delay(300)
        return dummyNotices
    }
}