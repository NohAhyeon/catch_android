package com.umc.catchandroid.domain.repository

import com.umc.catchandroid.domain.model.Spec
import com.umc.catchandroid.domain.model.SpecCategoryCounts

interface SpecRepository {
    suspend fun getSpecs(category: String, sort: String = "latest"): Pair<SpecCategoryCounts?, List<Spec>>
    suspend fun addSpec(spec: Spec): Pair<SpecCategoryCounts?, List<Spec>>
    suspend fun updateSpec(specId: Long, spec: Spec): Pair<SpecCategoryCounts?, List<Spec>>
    suspend fun deleteSpec(specId: Long): Pair<SpecCategoryCounts?, List<Spec>>
}