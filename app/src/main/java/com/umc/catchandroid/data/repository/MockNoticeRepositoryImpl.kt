//package com.umc.catchandroid.data.repository
//
//import com.umc.catchandroid.domain.model.AiSummary
//import com.umc.catchandroid.domain.model.Notice
//import com.umc.catchandroid.domain.model.NoticeDetail
//import com.umc.catchandroid.domain.repository.NoticeRepository
//import kotlinx.coroutines.delay
//import javax.inject.Inject
//
//class MockNoticeRepositoryImpl @Inject constructor() : NoticeRepository {
//
//    private val dummyNotices = listOf(
//        Notice(
//            noticeId = 1,
//            categoryTag = "장학",
//            title = "2026 1학기 국가장학금 신청 안내",
//            source = "학생복지처",
//            createdAt = "2026-07-13T09:00:00",
//            deadlineAt = "2026-07-15T23:59:59"
//        ),
//        Notice(
//            noticeId = 2,
//            categoryTag = "장학",
//            title = "글로벌 역량강화 프로그램 모집",
//            source = "국제교류처",
//            createdAt = "2026-07-13T04:00:00",
//            deadlineAt = "2026-07-22T18:00:00"
//        ),
//        Notice(
//            noticeId = 3,
//            categoryTag = "장학",
//            title = "교내 채용설명회 사전 신청 공지",
//            source = "취업지원팀",
//            createdAt = "2026-06-28T09:00:00",
//            deadlineAt = "2026-07-28T18:00:00"
//        ),
//        Notice(
//            noticeId = 4,
//            categoryTag = "취업",
//            title = "교내 채용설명회 사전 신청 공지",
//            source = "취업지원팀",
//            createdAt = "2026-06-28T09:00:00",
//            deadlineAt = "2026-07-28T18:00:00"
//        )
//    )
//
//    private val details = mapOf(
//        1L to NoticeDetail(
//            noticeId = 1,
//            categoryTag = "장학",
//            title = "2026 1학기 국가장학금 신청 안내",
//            source = "학생복지처",
//            createdAt = "2026-07-13T09:00:00",
//            deadlineAt = "2026-07-15T23:59:59",
//            content = "안녕하세요. 학생복지처입니다. 2026학년도 1학기 국가장학금 신청 일정을 다음과 같이 안내드립니다.\n\n신청 대상: 국내 대학(원)에 재학 중인 학부생\n신청 기간: 2026.07.01 ~ 2026.07.15 18:00\n신청 방법: 한국장학재단 홈페이지 온라인 신청",
//            hasFiles = true,
//            originalUrl = "https://www.university.ac.kr/notice/1001",
//            aiSummary = AiSummary(
//                eligibility = "국내 대학(원) 재학 중인 학부생",
//                benefit = "소득분위에 따라 등록금 일부~전액 지원",
//                deadline = "2026년 7월 15일 18:00까지"
//            )
//        ),
//        2L to NoticeDetail(
//            noticeId = 2,
//            categoryTag = "장학",
//            title = "글로벌 역량강화 프로그램 모집",
//            source = "국제교류처",
//            createdAt = "2026-07-13T04:00:00",
//            deadlineAt = "2026-07-22T18:00:00",
//            content = "국제교류처에서는 글로벌 역량강화를 위한 단기 프로그램 참가자를 모집합니다.\n\n모집 대상: 전체 재학생\n활동 기간: 2026.08.10 ~ 2026.08.20\n지원 내용: 항공료 및 체재비 일부 지원",
//            hasFiles = false,
//            originalUrl = "https://www.university.ac.kr/notice/1002",
//            aiSummary = AiSummary(
//                eligibility = "전체 재학생",
//                benefit = "항공료 및 체재비 일부 지원",
//                deadline = "2026년 7월 22일 18:00까지"
//            )
//        ),
//        3L to NoticeDetail(
//            noticeId = 3,
//            categoryTag = "장학",
//            title = "교내 채용설명회 사전 신청 공지",
//            source = "취업지원팀",
//            createdAt = "2026-06-28T09:00:00",
//            deadlineAt = "2026-07-28T18:00:00",
//            content = "교내 채용설명회가 개최됩니다. 참가를 희망하는 학생은 사전 신청해 주세요.\n\n일시: 2026.08.05 (수) 14:00\n장소: 학생회관 대강당",
//            hasFiles = true,
//            originalUrl = "https://www.university.ac.kr/notice/1003",
//            aiSummary = AiSummary(
//                eligibility = "전체 재학생 및 졸업예정자",
//                benefit = "참가 기업 채용 연계 우대",
//                deadline = "2026년 7월 28일 18:00까지"
//            )
//        ),
//        4L to NoticeDetail(
//            noticeId = 4,
//            categoryTag = "취업",
//            title = "교내 채용설명회 사전 신청 공지",
//            source = "취업지원팀",
//            createdAt = "2026-06-28T09:00:00",
//            deadlineAt = "2026-07-28T18:00:00",
//            content = "교내 채용설명회가 개최됩니다. 참가를 희망하는 학생은 사전 신청해 주세요.",
//            hasFiles = false,
//            originalUrl = "https://www.university.ac.kr/notice/1004",
//            aiSummary = null
//        )
//    )
//
//    override suspend fun getNotices(page: Int, size: Int): List<Notice> {
//        delay(300)
//        return dummyNotices
//    }
//
//    override suspend fun getNoticeDetail(noticeId: Long): NoticeDetail? {
//        delay(300)
//        return details[noticeId]
//    }
//}