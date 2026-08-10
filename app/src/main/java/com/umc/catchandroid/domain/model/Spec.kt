package com.umc.catchandroid.domain.model

data class Spec(
    val specId: Long,
    val category: String,
    val categoryTag: String,
    val title: String,
    val organization: String,
    val specDate: String,
    val scoreOrGrade: String? = null,
    val memo: String? = null
)

data class SpecCategoryCounts(
    val allCount: Int,
    val licenseCount: Int,
    val awardCount: Int,
    val activityCount: Int,
    val languageCount: Int,
    val internCount: Int,
    val etcCount: Int
)