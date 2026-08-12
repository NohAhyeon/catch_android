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
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class NoticeDetailViewModel @Inject constructor(
    private val noticeRepository: NoticeRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _detail = MutableStateFlow<NoticeDetail?>(null)
    val detail: StateFlow<NoticeDetail?> = _detail.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    // 재시도 버튼에서 같은 noticeId로 다시 부를 수 있도록 저장
    private var lastNoticeId: Long? = null

    fun load(noticeId: Long) {
        lastNoticeId = noticeId
        viewModelScope.launch {
            _errorMessage.value = null
            try {
                val result = noticeRepository.getNoticeDetail(noticeId)
                if (result != null) {
                    _detail.value = result
                } else {
                    _errorMessage.value = "존재하지 않거나 삭제된 공지예요"
                }
            } catch (e: IOException) {
                _errorMessage.value = "네트워크 연결을 확인해주세요"
            } catch (e: Exception) {
                _errorMessage.value = "공지를 불러오지 못했어요. 다시 시도해주세요"
            }
        }
    }

    fun retry() {
        lastNoticeId?.let { load(it) }
    }

    fun toggleScrap() {
        val current = _detail.value ?: return
        viewModelScope.launch {
            try {
                val newState = noticeRepository.toggleScrap(current.noticeId)
                _detail.value = current.copy(isScrapped = newState)
            } catch (e: Exception) {
                // 스크랩 토글 실패는 화면 전체를 막지 않고 조용히 무시
                // (필요하면 스낵바 등으로 "스크랩에 실패했어요" 안내 가능)
            }
        }
    }
}