package com.umc.catchandroid.presentation.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.umc.catchandroid.ui.theme.CatchTextBody
import com.umc.catchandroid.ui.theme.CatchTextCaption
import com.umc.catchandroid.ui.theme.CatchTextTitle

@Composable
fun OnboardingProfileScreen(
    onNext: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    var departmentSearch by remember { mutableStateOf("") }
    var department by remember { mutableStateOf("컴퓨터공학과") }
    var grade by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp)) {
        Text(
            text = "프로필 설정",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = CatchTextTitle,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth().padding(top = 20.dp, bottom = 16.dp)
        )
        HorizontalDivider(color = CatchInactive, thickness = 1.dp)

        Text(
            text = "맞춤 공지를 위해 학과·학년을 알려주세요.",
            fontSize = 13.sp,
            color = CatchTextCaption,
            modifier = Modifier.padding(top = 16.dp, bottom = 40.dp)
        )

        Text(
            text = "학과",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = CatchTextBody,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = departmentSearch,
            onValueChange = { departmentSearch = it },
            placeholder = { Text("학과 검색 (예:컴퓨터)", color = CatchTextCaption) },
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = CatchPrimary,
                unfocusedBorderColor = CatchInactive
            )
        )
        OutlinedTextField(
            value = department,
            onValueChange = { department = it },
            modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = CatchPrimary,
                unfocusedBorderColor = CatchPrimary
            )
        )

        Text(
            text = "학년",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = CatchTextBody,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = grade,
            onValueChange = { grade = it },
            placeholder = { Text("3학년", color = CatchTextCaption) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = CatchPrimary,
                unfocusedBorderColor = CatchInactive
            )
        )

        Column(modifier = Modifier.weight(1f)) {}

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
                .clickable {
                    val gradeNumber = grade.filter { it.isDigit() }.toIntOrNull() ?: 0
                    viewModel.saveProfile(department, gradeNumber)
                    onNext()
                }
        ) {
            Text(
                text = "다음",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}