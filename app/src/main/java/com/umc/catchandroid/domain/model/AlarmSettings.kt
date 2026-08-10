package com.umc.catchandroid.domain.model

data class AlarmSettings(
    val isAll: Boolean,
    val isClosing: Boolean,
    val isKeyword: Boolean,
    val scholarship: Boolean,
    val extracurricular: Boolean,
    val academic: Boolean,
    val employment: Boolean
)