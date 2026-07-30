package com.umc.catchandroid.data.remote

data class AlarmSettingsDto(
    val isAll: Boolean,
    val isClosing: Boolean,
    val isKeyword: Boolean,
    val scholarship: Boolean,
    val extracurricular: Boolean,
    val academic: Boolean,
    val employment: Boolean
)