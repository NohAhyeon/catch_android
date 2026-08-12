package com.umc.catchandroid.data.remote

data class UserProfileDto(
    val nickname: String,
    val universityName: String?,
    val departmentName: String?,
    val grade: Int?,
    val scrapCount: Int,
    val keywordCount: Int,
    val readCount: Int
)

data class UpdateGradeRequest(
    val grade: Int
)

data class UpdateKeywordsRequest(
    val keywords: List<KeywordItemDto>
)

data class KeywordItemDto(
    val keyword: String,
    val keywordType: String // "RECOMMEND" or "CUSTOM"
)

data class KeywordListResponseDto(
    val content: List<KeywordItemDto>,
    val totalCount: Int
)