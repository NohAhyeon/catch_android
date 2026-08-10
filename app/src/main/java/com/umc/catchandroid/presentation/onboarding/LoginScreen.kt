package com.umc.catchandroid.presentation.onboarding

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.umc.catchandroid.BuildConfig
import com.umc.catchandroid.R
import com.umc.catchandroid.ui.theme.CatchTextCaption
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    onLoginSuccess: (String) -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    fun handleKakaoLogin() {
        val callback: (com.kakao.sdk.auth.model.OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                    // 사용자가 로그인 취소한 경우, 별도 처리 없음
                } else {
                    Toast.makeText(context, "카카오 로그인 실패", Toast.LENGTH_SHORT).show()
                }
            } else if (token != null) {
                viewModel.loginWithSocialToken(token.accessToken, "KAKAO") { success ->
                    if (success) {
                        onLoginSuccess("kakao")
                    } else {
                        Toast.makeText(context, "서버 로그인 실패", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
            UserApiClient.instance.loginWithKakaoTalk(context, callback = callback)
        } else {
            UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
        }
    }

    fun handleGoogleLogin() {
        coroutineScope.launch {
            try {
                val credentialManager = CredentialManager.create(context)

                val googleIdOption = GetGoogleIdOption.Builder()
                    .setServerClientId(BuildConfig.GOOGLE_SERVER_CLIENT_ID)
                    .setFilterByAuthorizedAccounts(false)
                    .build()

                val request = GetCredentialRequest.Builder()
                    .addCredentialOption(googleIdOption)
                    .build()

                val result = credentialManager.getCredential(
                    request = request,
                    context = context
                )

                val credential = GoogleIdTokenCredential.createFrom(result.credential.data)
                val idToken = credential.idToken

                viewModel.loginWithSocialToken(idToken, "GOOGLE") { success ->
                    if (success) {
                        onLoginSuccess("google")
                    } else {
                        Toast.makeText(context, "서버 로그인 실패", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: GetCredentialException) {
                e.printStackTrace()
                Toast.makeText(context, "구글 로그인 실패", Toast.LENGTH_SHORT).show()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Spacer(modifier = Modifier.height(80.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "공지캐치 로고",
                modifier = Modifier
                    .size(120.dp)
                    .padding(bottom = 20.dp)
            )

            Text(
                text = "공지캐치",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF232323)
            )
            Text(
                text = "흩어진 학교 공지를 한 곳에서!",
                fontSize = 15.sp,
                color = CatchTextCaption,
                modifier = Modifier.padding(top = 10.dp)
            )
        }

        Spacer(modifier = Modifier.height(180.dp))

        Column(modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)) {
            // 카카오 로그인 버튼 (실제 SDK 연동)
            Button(
                onClick = { handleKakaoLogin() },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFE812))
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_kakao),
                        contentDescription = "카카오",
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "카카오로 시작하기",
                        color = Color(0xFF232323),
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 구글 로그인 버튼 (실제 Credential Manager 연동)
            OutlinedButton(
                onClick = { handleGoogleLogin() },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_google),
                        contentDescription = "구글",
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "Google로 시작하기",
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 애플 로그인 버튼 (지원 예정, 임시 Mock)
            Button(
                onClick = { onLoginSuccess("apple") },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_apple),
                        contentDescription = "애플",
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "Apple로 시작하기",
                        color = Color.White,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }
    }
}