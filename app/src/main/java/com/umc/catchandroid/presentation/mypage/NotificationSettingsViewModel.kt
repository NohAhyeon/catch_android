package com.umc.catchandroid.presentation.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.catchandroid.domain.model.AlarmSettings
import com.umc.catchandroid.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationSettingsViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _settings = MutableStateFlow<AlarmSettings?>(null)
    val settings: StateFlow<AlarmSettings?> = _settings.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadSettings()
    }

    private fun loadSettings() {
        viewModelScope.launch {
            _settings.value = userRepository.getAlarmSettings()
            _isLoading.value = false
        }
    }

    fun toggleAll(checked: Boolean) {
        val current = _settings.value ?: return
        val updated = if (checked) {
            current.copy(isAll = true)
        } else {
            // 마스터 끄면 서버에서 하위 알림도 다 꺼져있어야 함 (ALARM4002 방지)
            current.copy(
                isAll = false,
                isClosing = false,
                isKeyword = false,
                scholarship = false,
                extracurricular = false,
                academic = false,
                employment = false
            )
        }
        _settings.value = updated
        save(updated)
    }

    fun toggleClosing(checked: Boolean) = updateField { it.copy(isClosing = checked) }
    fun toggleKeyword(checked: Boolean) = updateField { it.copy(isKeyword = checked) }
    fun toggleScholarship(checked: Boolean) = updateField { it.copy(scholarship = checked) }
    fun toggleExtracurricular(checked: Boolean) = updateField { it.copy(extracurricular = checked) }
    fun toggleAcademic(checked: Boolean) = updateField { it.copy(academic = checked) }
    fun toggleEmployment(checked: Boolean) = updateField { it.copy(employment = checked) }

    private fun updateField(transform: (AlarmSettings) -> AlarmSettings) {
        val current = _settings.value ?: return
        if (!current.isAll) return // 마스터 꺼진 상태에선 하위 토글 무시
        val updated = transform(current)
        _settings.value = updated
        save(updated)
    }

    private fun save(settings: AlarmSettings) {
        viewModelScope.launch {
            userRepository.updateAlarmSettings(settings)
        }
    }
}