package com.umc.catchandroid.domain.model

data class UserProfile(
    val nickname: String,
    val universityName: String,
    val departmentName: String,
    val grade: Int,
    val scrapCount: Int,
    val keywordCount: Int,
    val readCount: Int
)