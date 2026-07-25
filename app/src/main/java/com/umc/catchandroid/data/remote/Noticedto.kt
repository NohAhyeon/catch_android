package com.umc.catchandroid.data.remote

data class NoticeListResult(
    val content: List<NoticeDto>,
    val page: Int,
    val size: Int,
    val hasNext: Boolean
)

data class NoticeDto(
    val noticeId: Long,
    val categoryTag: String,
    val title: String,
    val source: String,
    val createdAt: String,
    val deadlineAt: String?
)