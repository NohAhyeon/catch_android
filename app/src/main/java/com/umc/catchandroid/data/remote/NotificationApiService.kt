package com.umc.catchandroid.data.remote

import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Query

interface NotificationApiService {
    @GET("api/v1/notifications")
    suspend fun getNotifications(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): ApiResponse<NotificationListResult>

    @PATCH("api/v1/notifications/read")
    suspend fun markAllAsRead(): ApiResponse<Unit>
}