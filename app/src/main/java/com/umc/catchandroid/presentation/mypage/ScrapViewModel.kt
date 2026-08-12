package com.umc.catchandroid.presentation.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.catchandroid.data.local.NetworkConnectivityObserver
import com.umc.catchandroid.domain.model.Notice
import com.umc.catchandroid.domain.repository.NoticeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class ScrapViewModel @Inject constructor(
    private val noticeRepository: NoticeRepository,
    private val networkConnectivityObserver: NetworkConnectivityObserver
) : ViewModel() {

    private val _scraps = MutableStateFlow<List<Notice>>(emptyList())
    val scraps: StateFlow<List<Notice>> = _scraps.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        loadScraps()

        viewModelScope.launch {
            networkConnectivityObserver.isOnline
                .drop(1)
                .filter { it }
                .collect {
                    loadScraps()
                }
        }
    }

    fun loadScraps() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                _scraps.value = noticeRepository.getScraps(page = 0, size = 20)
            } catch (e: IOException) {
                _errorMessage.value = "네트워크 연결을 확인해주세요"
            } catch (e: Exception) {
                _errorMessage.value = "스크랩 목록을 불러오지 못했어요. 다시 시도해주세요"
            } finally {
                _isLoading.value = false
            }
        }
    }
}