package com.umc.catchandroid.presentation.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.catchandroid.data.local.NetworkConnectivityObserver
import com.umc.catchandroid.domain.model.Notification
import com.umc.catchandroid.domain.repository.NotificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

private const val PAGE_SIZE = 20


private val DUMMY_NOTIFICATIONS = listOf(

    Notification(
        notificationId = 2L,
        noticeId = 102L,
        title = "관심 키워드 [장학금] 새 공지",
        message = "",
        notificationType = "KEYWORD",
        isRead = false,
        createdAt = "2026-08-12T08:30:00"
    ),
    Notification(
        notificationId = 3L,
        noticeId = 103L,
        title = "관심 키워드 [장학] 새 공지",
        message = "",
        notificationType = "KEYWORD",
        isRead = true,
        createdAt = "2026-08-11T15:20:00"
    )
)

// "이전 알림 보기"를 눌렀을 때 추가로 보여줄 더미 (마감된/지난) 알림
private val DUMMY_OLDER_NOTIFICATIONS = listOf(
    Notification(
        notificationId = 4L,
        noticeId = 104L,
        title = "교내 근로장학생 모집 마감",
        message = "",
        notificationType = "CLOSING",
        isRead = true,
        createdAt = "2026-08-05T10:00:00"
    ),
    Notification(
        notificationId = 5L,
        noticeId = 105L,
        title = "관심 키워드 [비교과] 새 공지",
        message = "",
        notificationType = "KEYWORD",
        isRead = true,
        createdAt = "2026-08-03T14:15:00"
    ),
    Notification(
        notificationId = 6L,
        noticeId = 106L,
        title = "학과 행사 참여 신청 마감",
        message = "",
        notificationType = "공지",
        isRead = true,
        createdAt = "2026-07-28T09:00:00"
    )
)

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val notificationRepository: NotificationRepository,
    private val networkConnectivityObserver: NetworkConnectivityObserver
) : ViewModel() {

    private val _notifications = MutableStateFlow<List<Notification>>(emptyList())
    val notifications: StateFlow<List<Notification>> = _notifications.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isLoadingMore = MutableStateFlow(false)
    val isLoadingMore: StateFlow<Boolean> = _isLoadingMore.asStateFlow()

    private val _hasMore = MutableStateFlow(true)
    val hasMore: StateFlow<Boolean> = _hasMore.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private var currentPage = 0
    private var didAutoRegisterReconnect = false
    private var usingDummyData = false

    fun loadNotifications() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            currentPage = 0
            try {
                val result = notificationRepository.getNotifications(page = currentPage, size = PAGE_SIZE)

                if (result.isEmpty()) {

                    usingDummyData = true
                    _notifications.value = DUMMY_NOTIFICATIONS
                    _hasMore.value = true // "이전 알림 보기" 버튼 UI 확인용으로 true
                } else {
                    usingDummyData = false
                    _notifications.value = result
                    _hasMore.value = result.size >= PAGE_SIZE
                }

                notificationRepository.markAllAsRead()
            } catch (e: IOException) {
                _errorMessage.value = "네트워크 연결을 확인해주세요"
            } catch (e: Exception) {
                _errorMessage.value = "알림을 불러오지 못했어요. 다시 시도해주세요"
            } finally {
                _isLoading.value = false
            }
        }

        if (!didAutoRegisterReconnect) {
            didAutoRegisterReconnect = true
            viewModelScope.launch {
                networkConnectivityObserver.isOnline
                    .drop(1)
                    .filter { it }
                    .collect {
                        loadNotifications()
                    }
            }
        }
    }

    fun loadMore() {
        if (_isLoadingMore.value || !_hasMore.value) return
        viewModelScope.launch {
            _isLoadingMore.value = true

            if (usingDummyData) {
                delay(500) // 로딩 인디케이터가 보이도록 잠깐 지연
                _notifications.value = _notifications.value + DUMMY_OLDER_NOTIFICATIONS
                _hasMore.value = false
                _isLoadingMore.value = false
                return@launch
            }

            val nextPage = currentPage + 1
            try {
                val result = notificationRepository.getNotifications(page = nextPage, size = PAGE_SIZE)
                if (result.isNotEmpty()) {
                    currentPage = nextPage
                    _notifications.value = _notifications.value + result
                }
                _hasMore.value = result.size >= PAGE_SIZE
            } catch (e: Exception) {
                // 이전 알림 더보기 실패는 조용히 무시
            } finally {
                _isLoadingMore.value = false
            }
        }
    }

    fun markAllAsRead() {
        viewModelScope.launch {
            try {
                notificationRepository.markAllAsRead()
                _notifications.value = _notifications.value.map { it.copy(isRead = true) }
            } catch (e: Exception) {
                // 조용히 무시
            }
        }
    }
}