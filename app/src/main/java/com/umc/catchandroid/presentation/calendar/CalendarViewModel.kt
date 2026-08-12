package com.umc.catchandroid.presentation.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.catchandroid.data.local.NetworkConnectivityObserver
import com.umc.catchandroid.data.local.SchoolInfoChangeNotifier
import com.umc.catchandroid.domain.model.Notice
import com.umc.catchandroid.domain.repository.CalendarRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import java.io.IOException
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val calendarRepository: CalendarRepository,
    private val networkConnectivityObserver: NetworkConnectivityObserver,
    private val schoolInfoChangeNotifier: SchoolInfoChangeNotifier
) : ViewModel() {

    private val _yearMonth = MutableStateFlow(YearMonth.now())
    val yearMonth: StateFlow<YearMonth> = _yearMonth.asStateFlow()

    private val _deadlineDates = MutableStateFlow<List<String>>(emptyList())
    val deadlineDates: StateFlow<List<String>> = _deadlineDates.asStateFlow()

    private val _selectedDate = MutableStateFlow(LocalDate.now().toString())
    val selectedDate: StateFlow<String> = _selectedDate.asStateFlow()

    private val _noticesForDate = MutableStateFlow<List<Notice>>(emptyList())
    val noticesForDate: StateFlow<List<Notice>> = _noticesForDate.asStateFlow()

    private val _upcomingNotices = MutableStateFlow<List<Notice>>(emptyList())
    val upcomingNotices: StateFlow<List<Notice>> = _upcomingNotices.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        loadMonth()
        loadUpcoming()
        onDateSelected(_selectedDate.value)

        // 네트워크 재연결 시 자동 새로고침
        viewModelScope.launch {
            networkConnectivityObserver.isOnline
                .drop(1)
                .filter { it }
                .collect {
                    retry()
                }
        }

        // 학교 정보가 바뀌면(어느 화면에서 바꿨든) 자동으로 새로고침
        // - 이게 없으면 예전 학교 데이터가 화면에 그대로 남아있게 됨
        viewModelScope.launch {
            schoolInfoChangeNotifier.changes.collect {
                retry()
            }
        }
    }

    fun retry() {
        loadMonth()
        loadUpcoming()
        onDateSelected(_selectedDate.value)
    }

    private fun loadMonth() {
        viewModelScope.launch {
            _errorMessage.value = null
            try {
                _deadlineDates.value = calendarRepository.getDeadlineDates(
                    _yearMonth.value.year,
                    _yearMonth.value.monthValue
                )
            } catch (e: IOException) {
                _errorMessage.value = "네트워크 연결을 확인해주세요"
            } catch (e: Exception) {
                _errorMessage.value = "캘린더 정보를 불러오지 못했어요"
            }
        }
    }

    private fun loadUpcoming() {
        viewModelScope.launch {
            _errorMessage.value = null
            try {
                val notices = calendarRepository.getUpcomingNotices()
                _upcomingNotices.value = notices.sortedWith(
                    compareBy(
                        { notice -> upcomingSortGroup(notice.deadlineAt) },
                        { notice -> notice.deadlineAt ?: "" }
                    )
                )
            } catch (e: IOException) {
                _errorMessage.value = "네트워크 연결을 확인해주세요"
            } catch (e: Exception) {
                _errorMessage.value = "다가오는 일정을 불러오지 못했어요"
            }
        }
    }

    private fun upcomingSortGroup(deadlineAt: String?): Int {
        if (deadlineAt == null) return 2 // 상시
        return try {
            val date = LocalDate.parse(deadlineAt.substring(0, 10))
            val today = LocalDate.now()
            when {
                date.isEqual(today) -> 0   // D-Day
                date.isAfter(today) -> 1   // 마감임박
                else -> 3                   // 마감 (이미 지남)
            }
        } catch (e: Exception) {
            2
        }
    }

    fun nextMonth() {
        _yearMonth.value = _yearMonth.value.plusMonths(1)
        loadMonth()
    }

    fun prevMonth() {
        _yearMonth.value = _yearMonth.value.minusMonths(1)
        loadMonth()
    }

    fun onDateSelected(date: String) {
        _selectedDate.value = date
        viewModelScope.launch {
            _errorMessage.value = null
            try {
                _noticesForDate.value = calendarRepository.getNoticesByDate(date)
            } catch (e: IOException) {
                _errorMessage.value = "네트워크 연결을 확인해주세요"
            } catch (e: Exception) {
                _errorMessage.value = "해당 날짜 일정을 불러오지 못했어요"
            }
        }
    }
}