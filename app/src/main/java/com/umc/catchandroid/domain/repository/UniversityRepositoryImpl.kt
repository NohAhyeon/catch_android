package com.umc.catchandroid.data.repository

import com.umc.catchandroid.data.remote.UniversityApiService
import com.umc.catchandroid.domain.model.Department
import com.umc.catchandroid.domain.model.University
import com.umc.catchandroid.domain.repository.UniversityRepository
import javax.inject.Inject

class UniversityRepositoryImpl @Inject constructor(
    private val api: UniversityApiService
) : UniversityRepository {

    override suspend fun getUniversities(): List<University> {
        return try {
            val response = api.getUniversities()
            response.result?.content?.map {
                University(it.universityId, it.universityName)
            } ?: emptyList()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun getDepartments(universityId: Long, keyword: String?): List<Department> {
        return try {
            val response = api.getDepartments(universityId, keyword)
            response.result?.content?.map {
                Department(it.departmentId, it.departmentName)
            } ?: emptyList()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun selectUniversity(universityId: Long): Boolean {
        return try {
            val response = api.selectUniversity(universityId)
            response.isSuccess
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}