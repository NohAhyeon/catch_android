package com.umc.catchandroid.data.repository

import com.umc.catchandroid.data.remote.DeviceTokenRequest
import com.umc.catchandroid.data.remote.NotificationApiService
import com.umc.catchandroid.domain.model.Notification
import com.umc.catchandroid.domain.repository.NotificationRepository
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val api: NotificationApiService
) : NotificationRepository {

    override suspend fun getNotifications(page: Int, size: Int): List<Notification> {
        val response = api.getNotifications(page, size)
        return response.result?.content?.map {
            Notification(
                notificationId = it.notificationId,
                noticeId = it.noticeId,
                notificationType = it.notificationType,
                title = it.title,
                message = it.message,
                isRead = it.isRead,
                createdAt = it.createdAt
            )
        } ?: emptyList()
    }

    override suspend fun markAllAsRead(): Boolean {
        return api.markAllAsRead().isSuccess
    }

    override suspend fun registerDeviceToken(token: String): Boolean {
        return api.registerDeviceToken(DeviceTokenRequest(pushToken = token)).isSuccess
    }
}