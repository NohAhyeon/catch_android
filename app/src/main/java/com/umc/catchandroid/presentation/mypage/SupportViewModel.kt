package com.umc.catchandroid.presentation.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.catchandroid.data.local.NetworkConnectivityObserver
import com.umc.catchandroid.domain.model.Faq
import com.umc.catchandroid.domain.model.SupportNotice
import com.umc.catchandroid.domain.repository.SupportRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class SupportViewModel @Inject constructor(
    private val supportRepository: SupportRepository,
    private val networkConnectivityObserver: NetworkConnectivityObserver
) : ViewModel() {

    private val _notices = MutableStateFlow<List<SupportNotice>>(emptyList())
    val notices: StateFlow<List<SupportNotice>> = _notices.asStateFlow()

    private val _faqs = MutableStateFlow<List<Faq>>(emptyList())
    val faqs: StateFlow<List<Faq>> = _faqs.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private var didAutoRegisterReconnect = false

    fun load() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                _notices.value = supportRepository.getSupportNotices(page = 0, size = 20)
                _faqs.value = supportRepository.getFaqs(category = "ACCOUNT")
            } catch (e: IOException) {
                _errorMessage.value = "네트워크 연결을 확인해주세요"
            } catch (e: Exception) {
                _errorMessage.value = "공지사항을 불러오지 못했어요. 다시 시도해주세요"
            } finally {
                _isLoading.value = false
            }
        }

        // load()가 화면 진입 시마다(LaunchedEffect) 호출되므로,
        // 재연결 감지는 최초 한 번만 등록
        if (!didAutoRegisterReconnect) {
            didAutoRegisterReconnect = true
            viewModelScope.launch {
                networkConnectivityObserver.isOnline
                    .drop(1)
                    .filter { it }
                    .collect {
                        load()
                    }
            }
        }
    }
}