package com.umc.catchandroid.data.repository

import com.umc.catchandroid.data.remote.NoticeApiService
import com.umc.catchandroid.domain.model.Notice
import com.umc.catchandroid.domain.repository.CalendarRepository
import javax.inject.Inject

class CalendarRepositoryImpl @Inject constructor(
    private val api: NoticeApiService
) : CalendarRepository {

    // 전체 공지를 한 번에 가져와 클라이언트에서 날짜별로 계산
    // 주의: 여기서 예외를 삼키면 ViewModel의 에러 UI가 절대 안 뜸 -> 그대로 전파
    private suspend fun getAllNotices(): List<Notice> {
        return api.getNotices(page = 0, size = 100).result?.content?.map {
            Notice(
                noticeId = it.noticeId,
                categoryTag = it.categoryTag,
                title = it.title,
                source = it.source,
                createdAt = it.createdAt,
                deadlineAt = it.deadlineAt
            )
        } ?: emptyList()
    }

    override suspend fun getDeadlineDates(year: Int, month: Int): List<String> {
        val monthStr = "%04d-%02d".format(year, month)
        return getAllNotices()
            .mapNotNull { it.deadlineAt }
            .filter { it.substring(0, 7) == monthStr }
            .map { it.substring(0, 10) }
            .distinct()
    }

    override suspend fun getNoticesByDate(date: String): List<Notice> {
        return getAllNotices().filter { it.deadlineAt?.substring(0, 10) == date }
    }

    override suspend fun getUpcomingNotices(): List<Notice> {
        // 이전에는 .filter { it.deadlineAt == null } 로 상시 공지만 걸러져서
        // 마감임박 공지가 전혀 안 보이던 버그가 있었음.
        // 마감임박 + 상시 공지 전부 반환하고, 정렬(마감임박 먼저)은 CalendarViewModel에서 처리.
        return getAllNotices()
    }
}