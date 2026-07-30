package com.umc.catchandroid.data.remote

data class SpecListResult(
    val categoryCounts: SpecCategoryCountsDto?,
    val content: List<SpecItemDto>
)

data class SpecCategoryCountsDto(
    val allCount: Int,
    val licenseCount: Int,
    val awardCount: Int,
    val activityCount: Int,
    val languageCount: Int,
    val internCount: Int,
    val etcCount: Int
)

data class SpecItemDto(
    val specId: Long,
    val category: String,
    val categoryTag: String,
    val title: String,
    val organization: String,
    val specDate: String,
    val scoreOrGrade: String? = null,
    val memo: String? = null
)

data class SpecUpsertRequest(
    val category: String,
    val title: String,
    val organization: String,
    val specDate: String,
    val scoreOrGrade: String? = null,
    val memo: String? = null
)