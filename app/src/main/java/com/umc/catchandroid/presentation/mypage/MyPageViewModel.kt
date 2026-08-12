package com.umc.catchandroid.presentation.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.catchandroid.data.local.TokenManager
import com.umc.catchandroid.domain.model.UserProfile
import com.umc.catchandroid.domain.repository.SpecRepository
import com.umc.catchandroid.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val specRepository: SpecRepository,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _userProfile = MutableStateFlow<UserProfile?>(null)
    val userProfile: StateFlow<UserProfile?> = _userProfile.asStateFlow()

    // 스펙 개수는 프로필 응답에 없어서(readCount는 "읽은 공지 수"라 무관) 별도로 불러옴
    private val _specCount = MutableStateFlow(0)
    val specCount: StateFlow<Int> = _specCount.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        refreshProfile()
        loadSpecCount()
    }

    fun refreshProfile() {
        viewModelScope.launch {
            _errorMessage.value = null
            try {
                _userProfile.value = userRepository.getUserProfile()
            } catch (e: IOException) {
                _errorMessage.value = "네트워크 연결을 확인해주세요"
            } catch (e: Exception) {
                _errorMessage.value = "프로필을 불러오지 못했어요. 다시 시도해주세요"
            }
        }
    }

    fun loadSpecCount() {
        viewModelScope.launch {
            try {
                val (counts, _) = specRepository.getSpecs(category = "ALL")
                _specCount.value = counts?.allCount ?: 0
            } catch (e: Exception) {
                // 스펙 개수는 부가 정보라 실패해도 화면 전체를 막지 않음
            }
        }
    }

    fun logout(onComplete: () -> Unit) {
        viewModelScope.launch {
            tokenManager.clearTokens()
            onComplete()
        }
    }
}