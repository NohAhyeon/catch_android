package com.umc.catchandroid.domain.model

data class NoticeDetail(
    val noticeId: Long,
    val categoryTag: String,
    val title: String,
    val source: String,
    val createdAt: String,
    val deadlineAt: String?,
    val content: String?,
    val originalUrl: String,
    val isScrapped: Boolean,
    val aiSummary: AiSummary?
)

data class AiSummary(
    val eligibility: String,
    val benefit: String,
    val deadline: String
)