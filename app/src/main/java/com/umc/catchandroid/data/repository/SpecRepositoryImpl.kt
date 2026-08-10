package com.umc.catchandroid.data.repository

import com.umc.catchandroid.data.remote.SpecApiService
import com.umc.catchandroid.data.remote.SpecCategoryCountsDto
import com.umc.catchandroid.data.remote.SpecItemDto
import com.umc.catchandroid.data.remote.SpecUpsertRequest
import com.umc.catchandroid.domain.model.Spec
import com.umc.catchandroid.domain.model.SpecCategoryCounts
import com.umc.catchandroid.domain.repository.SpecRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SpecRepositoryImpl @Inject constructor(
    private val specApiService: SpecApiService
) : SpecRepository {

    override suspend fun getSpecs(category: String, sort: String): Pair<SpecCategoryCounts?, List<Spec>> {
        val result = specApiService.getSpecs(category = category, sort = sort).result!!
        return result.categoryCounts?.toDomain() to result.content.map { it.toDomain() }
    }

    override suspend fun addSpec(spec: Spec): Pair<SpecCategoryCounts?, List<Spec>> {
        val result = specApiService.addSpec(spec.toRequest(), page = 0, size = 20).result!!
        return result.categoryCounts?.toDomain() to result.content.map { it.toDomain() }
    }

    override suspend fun updateSpec(specId: Long, spec: Spec): Pair<SpecCategoryCounts?, List<Spec>> {
        val result = specApiService.updateSpec(specId, spec.toRequest()).result!!
        return result.categoryCounts?.toDomain() to result.content.map { it.toDomain() }
    }

    override suspend fun deleteSpec(specId: Long): Pair<SpecCategoryCounts?, List<Spec>> {
        val result = specApiService.deleteSpec(specId).result!!
        return result.categoryCounts?.toDomain() to result.content.map { it.toDomain() }
    }
}

private fun SpecCategoryCountsDto.toDomain() = SpecCategoryCounts(
    allCount = allCount,
    licenseCount = licenseCount,
    awardCount = awardCount,
    activityCount = activityCount,
    languageCount = languageCount,
    internCount = internCount,
    etcCount = etcCount
)

private fun SpecItemDto.toDomain() = Spec(
    specId = specId,
    category = category,
    categoryTag = categoryTag,
    title = title,
    organization = organization,
    specDate = specDate,
    scoreOrGrade = scoreOrGrade,
    memo = memo
)

private fun Spec.toRequest() = SpecUpsertRequest(
    category = category,
    title = title,
    organization = organization,
    specDate = specDate,
    scoreOrGrade = scoreOrGrade,
    memo = memo
)