package com.umc.catchandroid.data.repository

import com.umc.catchandroid.data.remote.NoticeApiService
import com.umc.catchandroid.domain.model.Notice
import com.umc.catchandroid.domain.repository.SearchRepository
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val api: NoticeApiService
) : SearchRepository {

    override suspend fun searchNotices(keyword: String): List<Notice> {
        return try {
            val response = api.searchNotices(searchWord = keyword)
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
}