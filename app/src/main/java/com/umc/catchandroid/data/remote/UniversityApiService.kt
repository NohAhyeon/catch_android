package com.umc.catchandroid.data.remote

import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path
import retrofit2.http.Query

interface UniversityApiService {
    @GET("api/v1/universities")
    suspend fun getUniversities(): ApiResponse<UniversityListResult>

    @GET("api/v1/universities/{universityId}/departments")
    suspend fun getDepartments(
        @Path("universityId") universityId: Long,
        @Query("keyword") keyword: String? = null
    ): ApiResponse<DepartmentListResult>

    @PATCH("api/v1/users/university/{universityId}")
    suspend fun selectUniversity(
        @Path("universityId") universityId: Long
    ): ApiResponse<Unit?>
}