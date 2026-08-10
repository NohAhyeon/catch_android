package com.umc.catchandroid.domain.repository

import com.umc.catchandroid.domain.model.Notification

interface NotificationRepository {
    suspend fun getNotifications(page: Int, size: Int): List<Notification>
    suspend fun markAllAsRead(): Boolean
}