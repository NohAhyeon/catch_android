package com.umc.catchandroid.presentation.notice

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun NoticeDetailScreen(
    noticeId: Long,
    onBack: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text("공지 상세 화면 (noticeId: $noticeId)")
        Button(onClick = onBack) {
            Text("뒤로가기")
        }
    }
}