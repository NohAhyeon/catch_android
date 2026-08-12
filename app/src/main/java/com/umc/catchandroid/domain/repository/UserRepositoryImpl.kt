package com.umc.catchandroid.data.repository

import com.umc.catchandroid.data.remote.AlarmSettingsDto
import com.umc.catchandroid.data.remote.KeywordItemDto
import com.umc.catchandroid.data.remote.UpdateGradeRequest
import com.umc.catchandroid.data.remote.UpdateKeywordsRequest
import com.umc.catchandroid.data.remote.UserApiService
import com.umc.catchandroid.domain.model.AlarmSettings
import com.umc.catchandroid.domain.model.Keyword
import com.umc.catchandroid.domain.model.UserProfile
import com.umc.catchandroid.domain.repository.UserRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val userApiService: UserApiService
) : UserRepository {

    override suspend fun getUserProfile(): UserProfile {
        val response = userApiService.getUserProfile()

        val dto = response.result
            ?: throw IllegalStateException("사용자 프로필 응답의 result가 null입니다.")

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

    override suspend fun updateOnboardingInfo(
        departmentId: Long?,
        grade: Int?
    ) {
        if (departmentId != null && grade != null) {
            userApiService.updateDepartment(
                departmentId,
                UpdateGradeRequest(grade)
            )
        }
    }

    override suspend fun updateSchoolInfo(
        universityId: Long,
        departmentId: Long,
        grade: Int
    ) {
        userApiService.updateSchoolInfo(
            universityId,
            departmentId,
            UpdateGradeRequest(grade)
        )
    }

    override suspend fun getKeywords(): List<Keyword> {
        val response = userApiService.getKeywords()

        val dto = response.result
            ?: throw IllegalStateException("키워드 응답의 result가 null입니다.")

        return dto.content.map {
            Keyword(
                it.keyword,
                it.keywordType
            )
        }
    }

    override suspend fun updateKeywords(
        keywords: List<Pair<String, String>>
    ) {
        val request = UpdateKeywordsRequest(
            keywords = keywords.map { (keyword, type) ->
                KeywordItemDto(
                    keyword,
                    type
                )
            }
        )

        userApiService.updateKeywords(request)
    }

    override suspend fun getAlarmSettings(): AlarmSettings {
        val response = userApiService.getAlarmSettings()

        val dto = response.result
            ?: throw IllegalStateException("알림 설정 응답의 result가 null입니다.")

        return AlarmSettings(
            isAll = dto.isAll,
            isClosing = dto.isClosing,
            isKeyword = dto.isKeyword,
            scholarship = dto.scholarship,
            extracurricular = dto.extracurricular,
            academic = dto.academic,
            employment = dto.employment
        )
    }

    override suspend fun updateAlarmSettings(
        settings: AlarmSettings
    ): AlarmSettings {

        val response = userApiService.updateAlarmSettings(
            AlarmSettingsDto(
                isAll = settings.isAll,
                isClosing = settings.isClosing,
                isKeyword = settings.isKeyword,
                scholarship = settings.scholarship,
                extracurricular = settings.extracurricular,
                academic = settings.academic,
                employment = settings.employment
            )
        )

        val dto = response.result
            ?: throw IllegalStateException("알림 설정 수정 응답의 result가 null입니다.")

        return AlarmSettings(
            isAll = dto.isAll,
            isClosing = dto.isClosing,
            isKeyword = dto.isKeyword,
            scholarship = dto.scholarship,
            extracurricular = dto.extracurricular,
            academic = dto.academic,
            employment = dto.employment
        )
    }
}