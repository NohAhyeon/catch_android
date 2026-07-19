package com.umc.catchandroid.presentation.onboarding

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.umc.catchandroid.ui.theme.CatchDivider
import com.umc.catchandroid.ui.theme.CatchPrimary
import com.umc.catchandroid.ui.theme.CatchTextBody
import com.umc.catchandroid.ui.theme.CatchTextCaption
import com.umc.catchandroid.ui.theme.CatchTextTitle

@Composable
fun OnboardingProfileScreen(
    onNext: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    var department by remember { mutableStateOf("") }
    var grade by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        Text(
            text = "프로필 설정",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = CatchTextTitle,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "맞춤 공지를 위해 학과·학년을 알려주세요.",
            fontSize = 13.sp,
            color = CatchTextCaption,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Text(
            text = "학과",
            fontSize = 13.sp,
            color = CatchTextBody,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        OutlinedTextField(
            value = department,
            onValueChange = { department = it },
            placeholder = { Text("학년 검색 (예:컴퓨터)", color = CatchTextCaption) },
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = CatchPrimary,
                unfocusedBorderColor = CatchDivider
            )
        )

        Text(
            text = "학년",
            fontSize = 13.sp,
            color = CatchTextBody,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        OutlinedTextField(
            value = grade,
            onValueChange = { grade = it },
            placeholder = { Text("3학년", color = CatchTextCaption) },
            modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = CatchPrimary,
                unfocusedBorderColor = CatchDivider
            )
        )

        Column(modifier = Modifier.weight(1f)) {}

        Button(
            onClick = {
                val gradeNumber = grade.filter { it.isDigit() }.toIntOrNull() ?: 0
                viewModel.saveProfile(department, gradeNumber)
                onNext()
            },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = CatchPrimary)
        ) {
            Text("다음")
        }
    }
}