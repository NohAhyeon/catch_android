package com.umc.catchandroid.presentation.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.catchandroid.data.local.NetworkConnectivityObserver
import com.umc.catchandroid.domain.model.Notification
import com.umc.catchandroid.domain.repository.NotificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

private const val PAGE_SIZE = 20

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

    fun loadNotifications() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            currentPage = 0
            try {
                val result = notificationRepository.getNotifications(page = currentPage, size = PAGE_SIZE)
                _notifications.value = result
                _hasMore.value = result.size >= PAGE_SIZE
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