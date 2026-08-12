package com.umc.catchandroid.presentation.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.catchandroid.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
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

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

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

    fun clearError() {
        _errorMessage.value = null
    }

    fun save(onDone: () -> Unit) {
        val recommendPairs = _selectedRecommended.value.map { it to "RECOMMEND" }
        val customPairs = _customKeywords.value.map { it to "CUSTOM" }
        val allPairs = recommendPairs + customPairs

        // 서버가 빈 키워드 목록을 400으로 거부하므로, 호출 전에 미리 막아서
        // 사용자에게 안내 메시지를 보여줌 (크래시 방지)
        if (allPairs.isEmpty()) {
            _errorMessage.value = "관심 키워드를 1개 이상 선택해주세요"
            return
        }

        viewModelScope.launch {
            try {
                userRepository.updateKeywords(allPairs)
                onDone()
            } catch (e: HttpException) {
                _errorMessage.value = "저장에 실패했어요. 키워드를 1개 이상 선택했는지 확인해주세요"
            } catch (e: Exception) {
                _errorMessage.value = "저장에 실패했어요. 다시 시도해주세요"
            }
        }
    }
}