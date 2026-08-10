package com.umc.catchandroid.presentation.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.catchandroid.domain.model.Notice
import com.umc.catchandroid.domain.repository.NoticeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScrapViewModel @Inject constructor(
    private val noticeRepository: NoticeRepository
) : ViewModel() {

    private val _scraps = MutableStateFlow<List<Notice>>(emptyList())
    val scraps: StateFlow<List<Notice>> = _scraps.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadScraps()
    }

    fun loadScraps() {
        viewModelScope.launch {
            _isLoading.value = true
            _scraps.value = noticeRepository.getScraps(page = 0, size = 20)
            _isLoading.value = false
        }
    }
}