package com.umc.catchandroid.domain.repository

import com.umc.catchandroid.domain.model.Notice
import com.umc.catchandroid.domain.model.NoticeDetail

interface NoticeRepository {
    suspend fun getNotices(page: Int, size: Int): List<Notice>
    suspend fun getNoticeDetail(noticeId: Long): NoticeDetail?
}