package com.umc.catchandroid.domain.model

data class Notification(
    val notificationId: Long,
    val noticeId: Long,
    val notificationType: String,
    val title: String,
    val message: String,
    val isRead: Boolean,
    val createdAt: String
)