package com.umc.catchandroid.presentation.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.catchandroid.domain.model.Faq
import com.umc.catchandroid.domain.model.SupportNotice
import com.umc.catchandroid.domain.repository.SupportRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SupportViewModel @Inject constructor(
    private val supportRepository: SupportRepository
) : ViewModel() {

    private val _notices = MutableStateFlow<List<SupportNotice>>(emptyList())
    val notices: StateFlow<List<SupportNotice>> = _notices.asStateFlow()

    private val _faqs = MutableStateFlow<List<Faq>>(emptyList())
    val faqs: StateFlow<List<Faq>> = _faqs.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun load() {
        viewModelScope.launch {
            _isLoading.value = true
            _notices.value = supportRepository.getSupportNotices(page = 0, size = 20)
            _faqs.value = supportRepository.getFaqs(category = "ACCOUNT")
            _isLoading.value = false
        }
    }
}