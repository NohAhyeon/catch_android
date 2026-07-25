package com.umc.catchandroid.presentation.onboarding

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.umc.catchandroid.R
import com.umc.catchandroid.ui.theme.CatchTextCaption

@Composable
fun LoginScreen(
    onLoginSuccess: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Spacer(modifier = Modifier.height(80.dp))

        // 로고 + 텍스트 영역 (위쪽)
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

        // 로그인 버튼 영역 (아래쪽)
        Column(modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)) {
            // 카카오 로그인 버튼
            Button(
                onClick = { onLoginSuccess("kakao") },
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

            // 구글 로그인 버튼
            OutlinedButton(
                onClick = { onLoginSuccess("google") },
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

            // 애플 로그인 버튼
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