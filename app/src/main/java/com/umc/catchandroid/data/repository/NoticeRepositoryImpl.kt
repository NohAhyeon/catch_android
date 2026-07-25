package com.umc.catchandroid.data.repository

import com.umc.catchandroid.data.remote.NoticeApiService
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
        // 공지 상세 API는 다음 단계에서 연동 예정
        return null
    }
}