package com.umc.catchandroid.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface NoticeApiService {
    @GET("api/v1/notices")
    suspend fun getNotices(
        @Query("page") page: Int,
        @Query("size") size: Int
    ): ApiResponse<NoticeListResult>
}