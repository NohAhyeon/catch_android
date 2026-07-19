package com.umc.catchandroid.presentation.notice

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.catchandroid.domain.model.NoticeDetail
import com.umc.catchandroid.domain.repository.NoticeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoticeDetailViewModel @Inject constructor(
    private val noticeRepository: NoticeRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _detail = MutableStateFlow<NoticeDetail?>(null)
    val detail: StateFlow<NoticeDetail?> = _detail.asStateFlow()

    fun load(noticeId: Long) {
        viewModelScope.launch {
            _detail.value = noticeRepository.getNoticeDetail(noticeId)
        }
    }
}