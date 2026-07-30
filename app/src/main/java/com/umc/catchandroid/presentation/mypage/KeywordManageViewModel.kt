package com.umc.catchandroid.presentation.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.catchandroid.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class KeywordManageViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _selectedRecommended = MutableStateFlow<Set<String>>(emptySet())
    val selectedRecommended: StateFlow<Set<String>> = _selectedRecommended.asStateFlow()

    private val _customKeywords = MutableStateFlow<Set<String>>(emptySet())
    val customKeywords: StateFlow<Set<String>> = _customKeywords.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadKeywords()
    }

    private fun loadKeywords() {
        viewModelScope.launch {
            val keywords = userRepository.getKeywords()
            _selectedRecommended.value = keywords
                .filter { it.type == "RECOMMEND" }
                .map { it.keyword }
                .toSet()
            _customKeywords.value = keywords
                .filter { it.type == "CUSTOM" }
                .map { it.keyword }
                .toSet()
            _isLoading.value = false
        }
    }

    fun toggleRecommended(keyword: String) {
        _selectedRecommended.value = if (_selectedRecommended.value.contains(keyword)) {
            _selectedRecommended.value - keyword
        } else {
            _selectedRecommended.value + keyword
        }
    }

    fun addCustomKeyword(keyword: String) {
        _customKeywords.value = _customKeywords.value + keyword
    }

    fun removeCustomKeyword(keyword: String) {
        _customKeywords.value = _customKeywords.value - keyword
    }

    fun save(onDone: () -> Unit) {
        viewModelScope.launch {
            val recommendPairs = _selectedRecommended.value.map { it to "RECOMMEND" }
            val customPairs = _customKeywords.value.map { it to "CUSTOM" }
            userRepository.updateKeywords(recommendPairs + customPairs)
            onDone()
        }
    }
}