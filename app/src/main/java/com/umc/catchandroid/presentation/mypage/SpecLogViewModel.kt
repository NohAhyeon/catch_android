package com.umc.catchandroid.presentation.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.catchandroid.domain.model.Spec
import com.umc.catchandroid.domain.model.SpecCategoryCounts
import com.umc.catchandroid.domain.repository.SpecRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SpecLogViewModel @Inject constructor(
    private val specRepository: SpecRepository
) : ViewModel() {

    private val _specs = MutableStateFlow<List<Spec>>(emptyList())
    val specs: StateFlow<List<Spec>> = _specs.asStateFlow()

    private val _categoryCounts = MutableStateFlow<SpecCategoryCounts?>(null)
    val categoryCounts: StateFlow<SpecCategoryCounts?> = _categoryCounts.asStateFlow()

    private val _selectedCategory = MutableStateFlow("ALL")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadSpecs()
    }

    fun selectCategory(category: String) {
        _selectedCategory.value = category
        loadSpecs()
    }

    fun loadSpecs() {
        viewModelScope.launch {
            _isLoading.value = true
            val (counts, list) = specRepository.getSpecs(category = _selectedCategory.value)
            _categoryCounts.value = counts
            _specs.value = list
            _isLoading.value = false
        }
    }
}