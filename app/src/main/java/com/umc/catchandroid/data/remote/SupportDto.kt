package com.umc.catchandroid.data.remote

data class SupportNoticeDto(
    val supportNoticeId: Long,
    val title: String,
    val content: String,
    val createdAt: String
)

data class SupportNoticeListResult(
    val content: List<SupportNoticeDto>,
    val page: Int,
    val size: Int,
    val hasNext: Boolean
)

data class FaqDto(
    val faqId: Long,
    val category: String,
    val question: String,
    val answer: String
)

data class FaqListResult(
    val content: List<FaqDto>,
    val totalCount: Int
)