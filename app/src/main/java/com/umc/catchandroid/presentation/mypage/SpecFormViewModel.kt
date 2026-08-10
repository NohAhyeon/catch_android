package com.umc.catchandroid.presentation.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.catchandroid.domain.model.Spec
import com.umc.catchandroid.domain.repository.SpecRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class SpecFormViewModel @Inject constructor(
    private val specRepository: SpecRepository
) : ViewModel() {

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun clearError() {
        _errorMessage.value = null
    }

    fun addSpec(spec: Spec, onDone: () -> Unit) {
        viewModelScope.launch {
            try {
                specRepository.addSpec(spec)
                onDone()
            } catch (e: HttpException) {
                _errorMessage.value = "저장에 실패했어요. 필수 항목(제목/카테고리/취득일)을 확인해주세요."
            }
        }
    }

    fun updateSpec(specId: Long, spec: Spec, onDone: () -> Unit) {
        viewModelScope.launch {
            try {
                specRepository.updateSpec(specId, spec)
                onDone()
            } catch (e: HttpException) {
                _errorMessage.value = "수정에 실패했어요. 필수 항목(제목/카테고리/취득일)을 확인해주세요."
            }
        }
    }

    fun deleteSpec(specId: Long, onDone: () -> Unit) {
        viewModelScope.launch {
            try {
                specRepository.deleteSpec(specId)
                onDone()
            } catch (e: HttpException) {
                _errorMessage.value = "삭제에 실패했어요."
            }
        }
    }
}