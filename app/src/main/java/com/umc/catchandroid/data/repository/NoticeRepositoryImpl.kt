package com.umc.catchandroid.data.repository

import com.umc.catchandroid.data.remote.NoticeApiService
import com.umc.catchandroid.domain.model.AiSummary
import com.umc.catchandroid.domain.model.Notice
import com.umc.catchandroid.domain.model.NoticeDetail
import com.umc.catchandroid.domain.repository.NoticeRepository
import javax.inject.Inject

class NoticeRepositoryImpl @Inject constructor(
    private val api: NoticeApiService
) : NoticeRepository {

    override suspend fun getNotices(page: Int, size: Int): List<Notice> {
        return try {
            val response = api.getNotices(page, size)
            response.result?.content?.map {
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

    override suspend fun getNoticeDetail(noticeId: Long): NoticeDetail? {
        return try {
            val response = api.getNoticeDetail(noticeId)
            response.result?.let { dto ->
                NoticeDetail(
                    noticeId = dto.noticeId,
                    categoryTag = dto.categoryTag,
                    title = dto.title,
                    source = dto.source,
                    createdAt = dto.createdAt,
                    deadlineAt = dto.deadlineAt,
                    content = dto.content,
                    originalUrl = dto.originalUrl,
                    isScrapped = dto.isScrapped,
                    aiSummary = dto.aiSummary?.let {
                        AiSummary(
                            eligibility = it.eligibility,
                            benefit = it.benefit,
                            deadline = it.deadline
                        )
                    }
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override suspend fun toggleScrap(noticeId: Long): Boolean {
        return try {
            api.toggleScrap(noticeId).result?.isScraped ?: false
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}