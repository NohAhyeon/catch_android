package com.umc.catchandroid.domain.model

data class Notice(
    val noticeId: Long,
    val categoryTag: String,
    val title: String,
    val source: String,
    val createdAt: String,
    val deadlineAt: String?,
    val isRead: Boolean = false
)