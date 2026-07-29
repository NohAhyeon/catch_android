package com.umc.catchandroid.presentation.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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

@Composable
fun OnboardingProfileScreen(
    universityId: Long,
    onNext: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    val departments by viewModel.departments.collectAsState()

    var departmentSearch by remember { mutableStateOf("") }
    var selectedDepartment by remember { mutableStateOf<Department?>(null) }
    var grade by remember { mutableStateOf("") }

    LaunchedEffect(departmentSearch) {
        if (departmentSearch.length >= 1) {
            viewModel.loadDepartments(universityId, departmentSearch)
        }
    }

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
        OutlinedTextField(
            value = departmentSearch,
            onValueChange = {
                departmentSearch = it
                selectedDepartment = null
            },
            placeholder = { Text("학과 검색 (예:컴퓨터)", color = CatchTextCaption) },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = CatchPrimary,
                unfocusedBorderColor = CatchInactive
            )
        )

        // 검색 결과 드롭다운 (선택 전까지만 표시)
        if (selectedDepartment == null && departments.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .padding(bottom = 16.dp)
            ) {
                items(departments) { dept ->
                    Text(
                        text = dept.departmentName,
                        fontSize = 14.sp,
                        color = CatchTextTitle,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                selectedDepartment = dept
                                departmentSearch = dept.departmentName
                            }
                            .padding(vertical = 10.dp)
                    )
                }
            }
        } else {
            Box(modifier = Modifier.padding(bottom = 24.dp)) {}
        }

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
                    val deptId = selectedDepartment?.departmentId
                    val gradeNumber = grade.filter { it.isDigit() }.toIntOrNull() ?: 0
                    if (deptId != null) {
                        viewModel.saveProfile(deptId, gradeNumber)
                        onNext()
                    }
                    // deptId == null이면 리스트에서 학과를 선택하지 않은 상태 → 무시 (또는 추후 안내 메시지 추가)
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