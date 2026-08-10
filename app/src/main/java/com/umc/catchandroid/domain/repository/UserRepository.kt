package com.umc.catchandroid.domain.repository

import com.umc.catchandroid.domain.model.AlarmSettings
import com.umc.catchandroid.domain.model.Keyword
import com.umc.catchandroid.domain.model.UserProfile

interface UserRepository {
    suspend fun getUserProfile(): UserProfile
    suspend fun updateOnboardingInfo(
        departmentId: Long? = null,
        grade: Int? = null
    )
    suspend fun updateSchoolInfo(universityId: Long, departmentId: Long, grade: Int)
    suspend fun getKeywords(): List<Keyword>
    suspend fun updateKeywords(keywords: List<Pair<String, String>>) // (keyword, keywordType)
    suspend fun getAlarmSettings(): AlarmSettings
    suspend fun updateAlarmSettings(settings: AlarmSettings): AlarmSettings
}