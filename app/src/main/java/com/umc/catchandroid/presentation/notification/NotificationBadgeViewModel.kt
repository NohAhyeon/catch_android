package com.umc.catchandroid.presentation.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.messaging.FirebaseMessaging
import com.umc.catchandroid.domain.repository.NotificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class NotificationBadgeViewModel @Inject constructor(
    private val notificationRepository: NotificationRepository
) : ViewModel() {

    private val _hasUnread = MutableStateFlow(false)
    val hasUnread: StateFlow<Boolean> = _hasUnread.asStateFlow()

    fun refresh() {
        viewModelScope.launch {
            try {
                val notifications = notificationRepository.getNotifications(page = 0, size = 20)
                _hasUnread.value = notifications.any { !it.isRead }
            } catch (e: Exception) {
                // 배지는 부가 기능이라 실패해도 앱을 막지 않고 조용히 무시
            }
        }
    }

    fun clear() {
        _hasUnread.value = false
    }

    // 로그인된 상태로 앱에 진입할 때마다 현재 FCM 토큰을 서버에 등록.
    // FcmService.onNewToken()은 토큰이 새로 발급/갱신될 때만 불리므로,
    // 이미 발급된 토큰을 매번 확실히 서버에 반영하기 위해 여기서도 한 번 더 시도함.
    fun registerFcmToken() {
        viewModelScope.launch {
            try {
                val token = FirebaseMessaging.getInstance().token.await()
                notificationRepository.registerDeviceToken(token)
            } catch (e: Exception) {
                // 로그인 전이거나 Play Services 문제 등으로 실패할 수 있음 - 조용히 무시
            }
        }
    }
}