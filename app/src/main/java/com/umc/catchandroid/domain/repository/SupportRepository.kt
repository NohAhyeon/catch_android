package com.umc.catchandroid.domain.repository

import com.umc.catchandroid.domain.model.Faq
import com.umc.catchandroid.domain.model.SupportNotice

interface SupportRepository {
    suspend fun getSupportNotices(page: Int, size: Int): List<SupportNotice>
    suspend fun getFaqs(category: String): List<Faq>
}