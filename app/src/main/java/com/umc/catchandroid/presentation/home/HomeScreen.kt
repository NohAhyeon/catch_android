package com.umc.catchandroid.presentation.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun HomeScreen(
    onNoticeClick: (Long) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text("홈 화면 - 공지 목록")
        Button(onClick = { onNoticeClick(1L) }) {
            Text("공지 상세로 이동 (테스트)")
        }
    }
}