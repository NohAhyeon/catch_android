package com.umc.catchandroid.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface SupportApiService {
    @GET("api/v1/support/notices")
    suspend fun getSupportNotices(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): ApiResponse<SupportNoticeListResult>

    @GET("api/v1/support/faqs")
    suspend fun getFaqs(
        @Query("category") category: String
    ): ApiResponse<FaqListResult>
}