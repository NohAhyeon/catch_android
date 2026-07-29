package com.umc.catchandroid.data.repository

import com.umc.catchandroid.data.remote.KeywordItemDto
import com.umc.catchandroid.data.remote.UpdateGradeRequest
import com.umc.catchandroid.data.remote.UpdateKeywordsRequest
import com.umc.catchandroid.data.remote.UserApiService
import com.umc.catchandroid.domain.model.UserProfile
import com.umc.catchandroid.domain.repository.UserRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val userApiService: UserApiService
) : UserRepository {

    override suspend fun getUserProfile(): UserProfile {
        val dto = userApiService.getUserProfile().result!!
        return UserProfile(
            nickname = dto.nickname,
            universityName = dto.universityName,
            departmentName = dto.departmentName,
            grade = dto.grade,
            scrapCount = dto.scrapCount,
            keywordCount = dto.keywordCount,
            readCount = dto.readCount
        )
    }

    override suspend fun updateOnboardingInfo(departmentId: Long?, grade: Int?) {
        if (departmentId != null && grade != null) {
            userApiService.updateDepartment(departmentId, UpdateGradeRequest(grade))
        }
    }

    override suspend fun updateKeywords(keywords: List<Pair<String, String>>) {
        val request = UpdateKeywordsRequest(
            keywords = keywords.map { (keyword, type) -> KeywordItemDto(keyword, type) }
        )
        userApiService.updateKeywords(request)
    }
}