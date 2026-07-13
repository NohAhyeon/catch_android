package com.umc.catchandroid.data.repository

import com.umc.catchandroid.domain.model.UserProfile
import com.umc.catchandroid.domain.repository.UserRepository
import kotlinx.coroutines.delay
import javax.inject.Inject

class MockUserRepositoryImpl @Inject constructor() : UserRepository {

    override suspend fun getUserProfile(): UserProfile {
        delay(300)
        return UserProfile(
            nickname = "홍길동",
            universityName = "영남대학교",
            departmentName = "컴퓨터공학과",
            grade = 3,
            scrapCount = 12,
            keywordCount = 5,
            readCount = 24
        )
    }
}