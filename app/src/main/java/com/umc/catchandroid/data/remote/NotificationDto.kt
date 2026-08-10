package com.umc.catchandroid.data.remote

data class NotificationDto(
    val notificationId: Long,
    val noticeId: Long,
    val notificationType: String,
    val title: String,
    val message: String,
    val isRead: Boolean,
    val createdAt: String
)

data class NotificationListResult(
    val content: List<NotificationDto>,
    val page: Int,
    val size: Int,
    val hasNext: Boolean
)