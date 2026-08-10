package com.umc.catchandroid.data.repository

import com.umc.catchandroid.data.remote.NotificationApiService
import com.umc.catchandroid.domain.model.Notification
import com.umc.catchandroid.domain.repository.NotificationRepository
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val api: NotificationApiService
) : NotificationRepository {

    override suspend fun getNotifications(page: Int, size: Int): List<Notification> {
        return try {
            val response = api.getNotifications(page, size)
            response.result?.content?.map {
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
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun markAllAsRead(): Boolean {
        return try {
            api.markAllAsRead().isSuccess
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}