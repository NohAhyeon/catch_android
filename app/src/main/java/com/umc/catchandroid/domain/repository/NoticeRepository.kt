package com.umc.catchandroid.domain.repository

import com.umc.catchandroid.domain.model.Notice

interface NoticeRepository {
    suspend fun getNotices(page: Int, size: Int): List<Notice>
}