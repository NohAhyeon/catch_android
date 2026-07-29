package com.umc.catchandroid.domain.repository

import com.umc.catchandroid.domain.model.UserProfile

interface UserRepository {
    suspend fun getUserProfile(): UserProfile
    suspend fun updateOnboardingInfo(
        departmentId: Long? = null,
        grade: Int? = null
    )
    suspend fun updateKeywords(keywords: List<Pair<String, String>>) // (keyword, keywordType)
}