package com.umc.catchandroid.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.catchandroid.domain.model.Notice
import com.umc.catchandroid.domain.model.UserProfile
import com.umc.catchandroid.domain.repository.NoticeRepository
import com.umc.catchandroid.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val noticeRepository: NoticeRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _notices = MutableStateFlow<List<Notice>>(emptyList())
    val notices: StateFlow<List<Notice>> = _notices.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _userProfile = MutableStateFlow<UserProfile?>(null)
    val userProfile: StateFlow<UserProfile?> = _userProfile.asStateFlow()

    init {
        loadNotices()
        loadProfile()
    }

    private fun loadNotices() {
        viewModelScope.launch {
            _isLoading.value = true
            val result = noticeRepository.getNotices(page = 0, size = 20)
            _notices.value = result.sortedWith(
                compareBy(
                    { notice -> sortGroup(notice.deadlineAt) },
                    { notice -> notice.deadlineAt ?: "" }
                )
            )
            _isLoading.value = false
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
            _userProfile.value = userRepository.getUserProfile()
        }
    }

    fun markAsRead(noticeId: Long) {
        _notices.value = _notices.value.map { notice ->
            if (notice.noticeId == noticeId) notice.copy(isRead = true) else notice
        }
    }
}