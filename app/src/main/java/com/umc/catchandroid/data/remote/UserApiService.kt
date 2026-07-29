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

    @PUT("api/v1/users/keywords")
    suspend fun updateKeywords(
        @Body request: UpdateKeywordsRequest
    ): ApiResponse<KeywordListResponseDto>
}