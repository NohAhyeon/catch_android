package com.umc.catchandroid.domain.repository

import com.umc.catchandroid.domain.model.UserProfile

interface UserRepository {
    suspend fun getUserProfile(): UserProfile
    suspend fun updateOnboardingInfo(
        university: String? = null,
        department: String? = null,
        grade: Int? = null
    )
}