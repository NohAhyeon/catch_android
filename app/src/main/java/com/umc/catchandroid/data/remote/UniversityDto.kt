package com.umc.catchandroid.data.remote

data class UniversityListResult(
    val content: List<UniversityDto>,
    val totalCount: Int
)

data class UniversityDto(
    val universityId: Long,
    val universityName: String
)

data class DepartmentListResult(
    val content: List<DepartmentDto>,
    val totalCount: Int
)

data class DepartmentDto(
    val departmentId: Long,
    val departmentName: String
)