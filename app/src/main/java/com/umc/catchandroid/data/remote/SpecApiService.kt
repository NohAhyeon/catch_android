package com.umc.catchandroid.data.remote

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface SpecApiService {

    @GET("api/v1/specs")
    suspend fun getSpecs(
        @Query("category") category: String,
        @Query("sort") sort: String = "latest",
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): ApiResponse<SpecListResult>

    @POST("api/v1/specs")
    suspend fun addSpec(
        @Body request: SpecUpsertRequest,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): ApiResponse<SpecListResult>

    @PUT("api/v1/specs/{specId}")
    suspend fun updateSpec(
        @Path("specId") specId: Long,
        @Body request: SpecUpsertRequest
    ): ApiResponse<SpecListResult>

    @DELETE("api/v1/specs/{specId}")
    suspend fun deleteSpec(
        @Path("specId") specId: Long
    ): ApiResponse<SpecListResult>
}