package com.umc.catchandroid.domain.repository

import com.umc.catchandroid.domain.model.Department
import com.umc.catchandroid.domain.model.University

interface UniversityRepository {
    suspend fun getUniversities(): List<University>
    suspend fun getDepartments(universityId: Long, keyword: String? = null): List<Department>
    suspend fun selectUniversity(universityId: Long): Boolean
}