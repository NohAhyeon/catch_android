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
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val calendarRepository: CalendarRepository
) : ViewModel() {

    private val _deadlineDates = MutableStateFlow<List<String>>(emptyList())
    val deadlineDates: StateFlow<List<String>> = _deadlineDates.asStateFlow()

    private val _selectedDate = MutableStateFlow<String?>(null)
    val selectedDate: StateFlow<String?> = _selectedDate.asStateFlow()

    private val _noticesForDate = MutableStateFlow<List<Notice>>(emptyList())
    val noticesForDate: StateFlow<List<Notice>> = _noticesForDate.asStateFlow()

    init {
        loadDeadlineDates()
    }

    private fun loadDeadlineDates() {
        viewModelScope.launch {
            _deadlineDates.value = calendarRepository.getDeadlineDates(2026, 7)
        }
    }

    fun onDateSelected(date: String) {
        _selectedDate.value = date
        viewModelScope.launch {
            _noticesForDate.value = calendarRepository.getNoticesByDate(date)
        }
    }
}