package com.umc.catchandroid.data.remote

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserApiService {

    @GET("api/v1/users/profile")
    suspend fun getUserProfile(): ApiResponse<UserProfileDto>

    @PATCH("api/v1/users/department/{departmentId}")
    suspend fun updateDepartment(
        @Path("departmentId") departmentId: Long,
        @Body request: UpdateGradeRequest
    ): ApiResponse<Unit>

    @PATCH("api/v1/users/profile/universities/{universityId}/departments/{departmentId}")
    suspend fun updateSchoolInfo(
        @Path("universityId") universityId: Long,
        @Path("departmentId") departmentId: Long,
        @Body request: UpdateGradeRequest
    ): ApiResponse<UserProfileDto>

    @GET("api/v1/users/keywords")
    suspend fun getKeywords(): ApiResponse<KeywordListResponseDto>

    @PUT("api/v1/users/keywords")
    suspend fun updateKeywords(
        @Body request: UpdateKeywordsRequest
    ): ApiResponse<KeywordListResponseDto>

    @GET("api/v1/users/alarm")
    suspend fun getAlarmSettings(): ApiResponse<AlarmSettingsDto>

    @PATCH("api/v1/users/alarm")
    suspend fun updateAlarmSettings(
        @Body request: AlarmSettingsDto
    ): ApiResponse<AlarmSettingsDto>
}