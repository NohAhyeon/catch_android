package com.umc.catchandroid.presentation.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import com.umc.catchandroid.domain.model.Department
import com.umc.catchandroid.ui.theme.CatchInactive
import com.umc.catchandroid.ui.theme.CatchPrimary
import com.umc.catchandroid.ui.theme.CatchSecondary
import com.umc.catchandroid.ui.theme.CatchTextBody
import com.umc.catchandroid.ui.theme.CatchTextCaption
import com.umc.catchandroid.ui.theme.CatchTextTitle

// label과 서버에 보낼 grade 숫자 매핑 (졸업유예는 5로 전송)
private val gradeOptions = listOf(
    "1학년" to 1,
    "2학년" to 2,
    "3학년" to 3,
    "4학년" to 4,
    "5학년 이상" to 5
)

@Composable
fun OnboardingProfileScreen(
    universityId: Long,
    selectedDepartment: Department?,
    onDepartmentSearchClick: () -> Unit,
    onNext: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    var selectedGrade by remember { mutableStateOf<Int?>(null) }

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
            modifier = Modifier.padding(top = 16.dp, bottom = 24.dp)
        )

        Text(
            text = "학과",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = CatchTextBody,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // 학과 검색 필드 (클릭 시 검색 화면으로 이동)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onDepartmentSearchClick() }
                .background(Color.White, RoundedCornerShape(12.dp))
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp)
            ) {
                Text(
                    text = selectedDepartment?.departmentName ?: "학과 검색 (예: 컴퓨터)",
                    fontSize = 15.sp,
                    color = if (selectedDepartment != null) CatchTextTitle else CatchTextCaption
                )
            }
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "학과 검색으로 이동",
                tint = CatchTextCaption,
                modifier = Modifier.padding(end = 12.dp)
            )
        }
        HorizontalDivider(color = CatchInactive, thickness = 1.dp)

        Text(
            text = "학년",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = CatchTextBody,
            modifier = Modifier.padding(top = 24.dp, bottom = 8.dp)
        )

        // 학년 선택 칩
        Row(modifier = Modifier.fillMaxWidth()) {
            gradeOptions.forEachIndexed { index, (label, value) ->
                GradeChip(
                    text = label,
                    selected = selectedGrade == value,
                    onClick = { selectedGrade = value },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = if (index != gradeOptions.lastIndex) 6.dp else 0.dp)
                )
            }
        }

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
                    val deptId = selectedDepartment?.departmentId
                    val gradeNumber = selectedGrade
                    if (deptId != null && gradeNumber != null) {
                        viewModel.saveProfile(deptId, gradeNumber)
                        onNext()
                    }
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

@Composable
private fun GradeChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .height(44.dp)
            .background(
                color = if (selected) CatchPrimary else Color.White,
                shape = RoundedCornerShape(10.dp)
            )
            .clickable(onClick = onClick)
    ) {
        Text(
            text = text,
            fontSize = 13.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
            color = if (selected) Color.White else CatchTextCaption
        )
    }
}