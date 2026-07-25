package com.umc.catchandroid.presentation.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.catchandroid.data.local.TokenManager
import com.umc.catchandroid.data.remote.AuthApiService
import com.umc.catchandroid.data.remote.LoginRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authApiService: AuthApiService,
    private val tokenManager: TokenManager
) : ViewModel() {

    fun loginWithSocialToken(
        socialToken: String,
        socialType: String,
        onResult: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response = authApiService.login(
                    LoginRequest(socialToken = socialToken, socialType = socialType)
                )
                val result = response.result
                if (response.isSuccess && result != null) {
                    tokenManager.saveTokens(result.accessToken, result.refreshToken)
                    onResult(true)
                } else {
                    onResult(false)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                onResult(false)
            }
        }
    }
}