package com.umc.catchandroid.presentation.mypage

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun MyPageScreen(
    viewModel: MyPageViewModel = hiltViewModel()
) {
    val profile by viewModel.userProfile.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        profile?.let { user ->
            Card(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "안녕하세요, ${user.nickname}님!")
                    Text(text = "${user.universityName} · ${user.departmentName} ${user.grade}학년")
                }
            }
            Row(modifier = Modifier.padding(vertical = 8.dp)) {
                Text(text = "스크랩 ${user.scrapCount}   ")
                Text(text = "관심키워드 ${user.keywordCount}   ")
                Text(text = "읽음 ${user.readCount}")
            }
        } ?: Text("로딩 중...")
    }
}