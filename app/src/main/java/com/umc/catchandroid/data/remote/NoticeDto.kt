package com.umc.catchandroid.data.remote

data class NoticeListResult(
    val content: List<NoticeItemDto>,
    val page: Int,
    val size: Int,
    val hasNext: Boolean
)

data class NoticeItemDto(
    val noticeId: Long,
    val categoryTag: String,
    val title: String,
    val source: String,
    val createdAt: String,
    val deadlineAt: String?
)

data class NoticeDetailDto(
    val noticeId: Long,
    val categoryTag: String,
    val title: String,
    val source: String,
    val createdAt: String,
    val deadlineAt: String?,
    val content: String?,
    val hasFiles: Boolean,
    val originalUrl: String,
    val isScrapped: Boolean,
    val aiSummary: AiSummaryDto?
)

data class AiSummaryDto(
    val eligibility: String,
    val benefit: String,
    val deadline: String
)

data class ScrapResultDto(
    val noticeId: Long,
    val isScraped: Boolean
)