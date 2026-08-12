package com.umc.catchandroid.presentation.component

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.catchandroid.data.local.NetworkConnectivityObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ConnectivityViewModel @Inject constructor(
    networkConnectivityObserver: NetworkConnectivityObserver
) : ViewModel() {
    // 실제 기기/에뮬레이터의 네트워크 상태를 그대로 반영 (API 호출 성공/실패와 무관)
    val isOnline: StateFlow<Boolean> = networkConnectivityObserver.isOnline
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = true // 초기값은 낙관적으로 true, 실제 값 들어오면 바로 갱신됨
        )
}