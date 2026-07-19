package com.umc.catchandroid.presentation.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.umc.catchandroid.R
import com.umc.catchandroid.ui.theme.CatchPrimary
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.layout.ContentScale



private val recommendedKeywords = listOf("장학금", "비교과", "학사", "취업", "대외활동", "교환학생")

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun OnboardingKeywordScreen(
    onComplete: () -> Unit
) {
    var selectedKeywords by remember { mutableStateOf(setOf<String>()) }
    var customInput by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        Text(
            text = "관심 키워드",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "관심사를 고르면 맞춤 공지를 받아요 (${selectedKeywords.size}/10)",
            fontSize = 13.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(text = "추천 키워드", fontSize = 13.sp, modifier = Modifier.padding(bottom = 8.dp))
        FlowRow(
            modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)
        ) {
            recommendedKeywords.forEach { keyword ->
                FilterChip(
                    selected = selectedKeywords.contains(keyword),
                    onClick = {
                        selectedKeywords = if (selectedKeywords.contains(keyword)) {
                            selectedKeywords - keyword
                        } else {
                            selectedKeywords + keyword
                        }
                    },
                    label = { Text(keyword) },
                    modifier = Modifier.padding(end = 8.dp, bottom = 8.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = CatchPrimary,
                        selectedLabelColor = Color.White
                    )
                )
            }
        }

        Text(text = "직접 입력", fontSize = 13.sp, modifier = Modifier.padding(bottom = 8.dp))
        OutlinedTextField(
            value = customInput,
            onValueChange = { customInput = it },
            placeholder = { Text("키워드 입력 후 Enter (2-20자)") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        )

        // 여백 채우고 캐릭터를 아래쪽에 배치
        Column(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Bottom
        ) {
            Image(
                painter = painterResource(id = R.drawable.character),
                contentDescription = "공지캐치 캐릭터",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .height(280.dp)
                    .fillMaxWidth(0.6f)
            )
        }

        Button(
            onClick = onComplete,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = CatchPrimary)
        ) {
            Text("완료하고 시작하기")
        }
    }
}