package com.umc.catchandroid.presentation.mypage

val specCategories = listOf(
    "LICENSE" to "자격증",
    "AWARD" to "수상",
    "ACTIVITY" to "대외활동",
    "LANGUAGE" to "어학",
    "INTERN" to "인턴",
    "ETC" to "기타"
)

fun specCategoryLabel(code: String): String =
    specCategories.find { it.first == code }?.second ?: code