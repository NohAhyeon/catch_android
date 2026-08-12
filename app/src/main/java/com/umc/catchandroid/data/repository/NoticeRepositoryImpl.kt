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

    // 주의: 여기서 예외를 잡아서 emptyList()/null 등으로 감춰버리면
    // ViewModel의 try-catch가 절대 발동하지 않아서 에러 UI가 안 뜸.
    // 그래서 이 파일은 실패를 그대로 던지고, 에러 처리는 ViewModel 쪽에서 담당함.

    override suspend fun getNotices(page: Int, size: Int): List<Notice> {
        val response = api.getNotices(page, size)
        return response.result?.content?.map {
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

    override suspend fun getNoticeDetail(noticeId: Long): NoticeDetail? {
        val response = api.getNoticeDetail(noticeId)
        return response.result?.let { dto ->
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
    }

    override suspend fun toggleScrap(noticeId: Long): Boolean {
        return api.toggleScrap(noticeId).result?.isScraped ?: false
    }

    override suspend fun getScraps(page: Int, size: Int): List<Notice> {
        val response = api.getScraps(page, size)
        return response.result?.content?.map {
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
}