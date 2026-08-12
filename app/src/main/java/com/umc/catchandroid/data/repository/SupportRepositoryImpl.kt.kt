package com.umc.catchandroid.data.repository

import com.umc.catchandroid.data.remote.SupportApiService
import com.umc.catchandroid.domain.model.Faq
import com.umc.catchandroid.domain.model.SupportNotice
import com.umc.catchandroid.domain.repository.SupportRepository
import javax.inject.Inject

class SupportRepositoryImpl @Inject constructor(
    private val api: SupportApiService
) : SupportRepository {

    override suspend fun getSupportNotices(page: Int, size: Int): List<SupportNotice> {
        val response = api.getSupportNotices(page, size)
        return response.result?.content?.map {
            SupportNotice(
                supportNoticeId = it.supportNoticeId,
                title = it.title,
                content = it.content,
                createdAt = it.createdAt
            )
        } ?: emptyList()
    }

    override suspend fun getFaqs(category: String): List<Faq> {
        val response = api.getFaqs(category)
        return response.result?.content?.map {
            Faq(
                faqId = it.faqId,
                category = it.category,
                question = it.question,
                answer = it.answer
            )
        } ?: emptyList()
    }
}