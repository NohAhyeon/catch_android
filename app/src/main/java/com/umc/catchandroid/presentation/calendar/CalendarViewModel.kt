package com.umc.catchandroid.presentation.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.catchandroid.domain.model.Notice
import com.umc.catchandroid.domain.repository.CalendarRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.YearMonth
import javax.inject.Inject
import java.time.LocalDate

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val calendarRepository: CalendarRepository
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

    init {
        loadMonth()
        loadUpcoming()
        onDateSelected(_selectedDate.value)
    }

    private fun loadMonth() {
        viewModelScope.launch {
            _deadlineDates.value = calendarRepository.getDeadlineDates(
                _yearMonth.value.year,
                _yearMonth.value.monthValue
            )
        }
    }

    private fun loadUpcoming() {
        viewModelScope.launch {
            _upcomingNotices.value = calendarRepository.getUpcomingNotices()
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
            _noticesForDate.value = calendarRepository.getNoticesByDate(date)
        }
    }
}