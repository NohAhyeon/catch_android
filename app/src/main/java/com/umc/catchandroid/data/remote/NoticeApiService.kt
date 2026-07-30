package com.umc.catchandroid.data.remote

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.POST

interface NoticeApiService {
    @GET("api/v1/notices")
    suspend fun getNotices(
        @Query("page") page: Int,
        @Query("size") size: Int
    ): ApiResponse<NoticeListResult>

    @GET("api/v1/notices/{noticeId}")
    suspend fun getNoticeDetail(
        @Path("noticeId") noticeId: Long
    ): ApiResponse<NoticeDetailDto>

    @GET("api/v1/notices/search")
    suspend fun searchNotices(
        @Query("searchWord") searchWord: String,
        @Query("sort") sort: String = "latest",
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): ApiResponse<NoticeListResult>

    @POST("api/v1/notices/{noticeId}/scrap")
    suspend fun toggleScrap(
        @Path("noticeId") noticeId: Long
    ): ApiResponse<ScrapResultDto>

    @GET("api/v1/notices/calendar")
    suspend fun getNoticesByDate(
        @Query("date") date: String,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): ApiResponse<NoticeListResult>

    @GET("api/v1/notices/calendar/no-deadline")
    suspend fun getNoDeadlineNotices(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): ApiResponse<NoticeListResult>

    @GET("api/v1/notices/calendar/dates")
    suspend fun getDeadlineDates(
        @Query("year") year: String,
        @Query("month") month: String
    ): ApiResponse<CalendarDatesResult>
}