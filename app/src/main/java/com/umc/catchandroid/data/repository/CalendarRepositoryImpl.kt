package com.umc.catchandroid.data.repository

import com.umc.catchandroid.data.remote.NoticeApiService
import com.umc.catchandroid.domain.model.Notice
import com.umc.catchandroid.domain.repository.CalendarRepository
import javax.inject.Inject

class CalendarRepositoryImpl @Inject constructor(
    private val api: NoticeApiService
) : CalendarRepository {

    override suspend fun getDeadlineDates(year: Int, month: Int): List<String> {
        return try {
            api.getDeadlineDates(year.toString(), month.toString()).result?.content ?: emptyList()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun getNoticesByDate(date: String): List<Notice> {
        return try {
            api.getNoticesByDate(date).result?.content?.map {
                Notice(
                    noticeId = it.noticeId,
                    categoryTag = it.categoryTag,
                    title = it.title,
                    source = it.source,
                    createdAt = it.createdAt,
                    deadlineAt = it.deadlineAt
                )
            } ?: emptyList()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun getUpcomingNotices(): List<Notice> {
        return try {
            api.getNoDeadlineNotices().result?.content?.map {
                Notice(
                    noticeId = it.noticeId,
                    categoryTag = it.categoryTag,
                    title = it.title,
                    source = it.source,
                    createdAt = it.createdAt,
                    deadlineAt = it.deadlineAt
                )
            } ?: emptyList()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}