package com.umc.catchandroid.presentation.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.umc.catchandroid.ui.theme.CatchInactive
import com.umc.catchandroid.ui.theme.CatchPrimary
import com.umc.catchandroid.ui.theme.CatchSecondary
import com.umc.catchandroid.ui.theme.CatchSecondaryLight
import com.umc.catchandroid.ui.theme.CatchTextCaption
import com.umc.catchandroid.ui.theme.CatchTextTitle

private fun providerLabel(provider: String): String = when (provider) {
    "kakao" -> "카카오로 시작하기"
    "google" -> "Google로 시작하기"
    "apple" -> "Apple로 시작하기"
    else -> "시작하기"
}

@Composable
fun OnboardingUniversityScreen(
    provider: String,
    onNext: (Long) -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    val universities by viewModel.universities.collectAsState()
    var selected by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        viewModel.loadUniversities()
    }

    // 목록 로드되면 첫 번째 항목을 기본 선택
    LaunchedEffect(universities) {
        if (selected == null && universities.isNotEmpty()) {
            selected = universities.first().universityName
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp)) {
        Text(
            text = "대학 선택",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = CatchTextTitle,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth().padding(top = 20.dp, bottom = 16.dp)
        )
        HorizontalDivider(color = CatchInactive, thickness = 1.dp)

        Text(
            text = "재학 중인 대학을 선택하세요 (이메일 인증 없이 선택)",
            fontSize = 13.sp,
            color = CatchTextCaption,
            modifier = Modifier.padding(top = 16.dp, bottom = 20.dp)
        )

        if (universities.isEmpty()) {
            // 로딩 중이거나, 아직 로그인 인증이 안 되어 목록을 못 받아온 상태
            Box(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = CatchPrimary)
            }
        } else {
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(universities) { univ ->
                    val isSelected = selected == univ.universityName
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                            .selectable(
                                selected = isSelected,
                                onClick = { selected = univ.universityName }
                            )
                            .background(
                                color = if (isSelected) CatchSecondaryLight else Color.White,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .border(
                                width = 1.dp,
                                color = if (isSelected) CatchPrimary else CatchInactive,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(horizontal = 16.dp, vertical = 16.dp)
                    ) {
                        Text(
                            text = univ.universityName,
                            fontSize = 16.sp,
                            color = CatchTextTitle,
                            modifier = Modifier.weight(1f)
                        )
                        RadioButton(
                            selected = isSelected,
                            onClick = { selected = univ.universityName },
                            colors = RadioButtonDefaults.colors(selectedColor = CatchPrimary)
                        )
                    }
                }
            }
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
                .height(56.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(CatchSecondary, CatchPrimary)
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
                .selectable(selected = false, onClick = {
                    val university = universities.find { it.universityName == selected }
                    selected?.let { viewModel.saveUniversity(it) }
                    onNext(university?.universityId ?: 1L)
                })
        ) {
            Text(
                text = providerLabel(provider),
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}