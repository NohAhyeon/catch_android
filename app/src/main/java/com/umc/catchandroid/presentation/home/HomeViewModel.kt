package com.umc.catchandroid.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.catchandroid.data.local.NetworkConnectivityObserver
import com.umc.catchandroid.data.local.SchoolInfoChangeNotifier
import com.umc.catchandroid.domain.model.Notice
import com.umc.catchandroid.domain.model.UserProfile
import com.umc.catchandroid.domain.repository.NoticeRepository
import com.umc.catchandroid.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import java.io.IOException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val noticeRepository: NoticeRepository,
    private val userRepository: UserRepository,
    private val schoolInfoChangeNotifier: SchoolInfoChangeNotifier,
    private val networkConnectivityObserver: NetworkConnectivityObserver
) : ViewModel() {

    private val _notices = MutableStateFlow<List<Notice>>(emptyList())
    val notices: StateFlow<List<Notice>> = _notices.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _userProfile = MutableStateFlow<UserProfile?>(null)
    val userProfile: StateFlow<UserProfile?> = _userProfile.asStateFlow()

    init {
        loadNotices()
        loadProfile()

        viewModelScope.launch {
            schoolInfoChangeNotifier.changes.collect {
                refresh()
            }
        }

        // 네트워크가 끊겼다가 다시 붙는 순간을 감지해서 자동으로 재요청
        // drop(1): 화면 진입 시 첫 상태값(이미 온라인)은 새로고침 트리거로 안 씀
        viewModelScope.launch {
            networkConnectivityObserver.isOnline
                .drop(1)
                .filter { it }
                .collect {
                    refresh()
                }
        }
    }

    fun refresh() {
        loadNotices()
        loadProfile()
    }

    private fun loadNotices() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val result = noticeRepository.getNotices(page = 0, size = 20)
                _notices.value = result.sortedWith(
                    compareBy(
                        { notice -> sortGroup(notice.deadlineAt) },
                        { notice -> notice.deadlineAt ?: "" }
                    )
                )
            } catch (e: IOException) {
                _errorMessage.value = "네트워크 연결을 확인해주세요"
            } catch (e: Exception) {
                _errorMessage.value = "공지를 불러오지 못했어요. 잠시 후 다시 시도해주세요"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun sortGroup(deadlineAt: String?): Int {
        if (deadlineAt == null) return 1
        return try {
            val date = LocalDate.parse(deadlineAt.substring(0, 10), DateTimeFormatter.ISO_DATE)
            if (date.isBefore(LocalDate.now())) 2 else 0
        } catch (e: Exception) {
            1
        }
    }

    private fun loadProfile() {
        viewModelScope.launch {
            try {
                _userProfile.value = userRepository.getUserProfile()
            } catch (e: Exception) {
                // 프로필은 부가 정보라 실패해도 화면 전체를 막지 않음
            }
        }
    }

    fun markAsRead(noticeId: Long) {
        _notices.value = _notices.value.map { notice ->
            if (notice.noticeId == noticeId) notice.copy(isRead = true) else notice
        }
    }
}