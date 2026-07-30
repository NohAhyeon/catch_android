package com.umc.catchandroid.presentation.mypage

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.umc.catchandroid.domain.model.Spec
import com.umc.catchandroid.ui.theme.CatchInactive
import com.umc.catchandroid.ui.theme.CatchPrimary
import com.umc.catchandroid.ui.theme.CatchTextTitle

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SpecDetailScreen(
    spec: Spec,
    onBack: () -> Unit,
    viewModel: SpecFormViewModel = hiltViewModel()
) {
    var category by remember { mutableStateOf(spec.category) }
    var title by remember { mutableStateOf(spec.title) }
    var organization by remember { mutableStateOf(spec.organization) }
    var specDate by remember { mutableStateOf(spec.specDate) }
    var scoreOrGrade by remember { mutableStateOf(spec.scoreOrGrade ?: "") }
    var memo by remember { mutableStateOf(spec.memo ?: "") }
    var showDeleteDialog by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 12.dp)) {
            IconButton(onClick = onBack, modifier = Modifier.align(Alignment.CenterStart)) {
                Icon(Icons.Default.ArrowBack, contentDescription = "뒤로", tint = CatchTextTitle)
            }
            Text(
                text = "스펙 상세",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = CatchTextTitle,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Text(
                text = "카테고리",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = CatchTextTitle,
                modifier = Modifier.padding(top = 8.dp, bottom = 12.dp)
            )
            FlowRow(modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)) {
                specCategories.forEach { (code, label) ->
                    val isSelected = category == code
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .padding(end = 8.dp, bottom = 8.dp)
                            .height(40.dp)
                            .background(
                                color = if (isSelected) CatchPrimary else Color.White,
                                shape = RoundedCornerShape(20.dp)
                            )
                            .border(
                                width = if (isSelected) 0.dp else 1.dp,
                                color = CatchInactive,
                                shape = RoundedCornerShape(20.dp)
                            )
                            .clickable { category = code }
                            .padding(horizontal = 20.dp)
                    ) {
                        Text(text = label, fontSize = 14.sp, color = if (isSelected) Color.White else CatchTextTitle)
                    }
                }
            }

            SpecTextField(label = "제목", value = title, onValueChange = { title = it }, placeholder = "예: 정보처리기사")
            SpecTextField(label = "기관 · 주최", value = organization, onValueChange = { organization = it }, placeholder = "예: 한국산업인력공단")
            SpecTextField(label = "취득일 (YYYY-MM-DD)", value = specDate, onValueChange = { specDate = it }, placeholder = "2026-07-30")
            SpecTextField(label = "점수 등급", value = scoreOrGrade, onValueChange = { scoreOrGrade = it }, placeholder = "예: 합격")
            SpecTextField(label = "메모 (선택, 최대 200자)", value = memo, onValueChange = { if (it.length <= 200) memo = it }, placeholder = "메모를 입력해주세요 (선택)")

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .height(56.dp)
                    .background(color = CatchPrimary, shape = RoundedCornerShape(12.dp))
                    .clickable {
                        viewModel.updateSpec(
                            spec.specId,
                            Spec(
                                specId = spec.specId,
                                category = category,
                                categoryTag = "",
                                title = title.trim(),
                                organization = organization.trim(),
                                specDate = specDate.trim(),
                                scoreOrGrade = scoreOrGrade.trim().ifBlank { null },
                                memo = memo.trim().ifBlank { null }
                            ),
                            onDone = onBack
                        )
                    }
            ) {
                Text("수정하기", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, bottom = 24.dp)
                    .height(56.dp)
                    .background(color = Color.White, shape = RoundedCornerShape(12.dp))
                    .border(width = 1.dp, color = Color(0xFFE05353), shape = RoundedCornerShape(12.dp))
                    .clickable { showDeleteDialog = true }
            ) {
                Text("삭제", color = Color(0xFFE05353), fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("스펙을 삭제하시겠어요?", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = CatchTextTitle) },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteDialog = false
                        viewModel.deleteSpec(spec.specId, onDone = onBack)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE05353))
                ) {
                    Text("삭제", color = Color.White)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showDeleteDialog = false }) {
                    Text("취소")
                }
            }
        )
    }
}