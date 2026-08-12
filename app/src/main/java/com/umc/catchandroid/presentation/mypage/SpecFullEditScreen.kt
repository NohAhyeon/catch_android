package com.umc.catchandroid.presentation.mypage

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import com.umc.catchandroid.ui.theme.CatchTextCaption
import com.umc.catchandroid.ui.theme.CatchTextTitle
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

private val specEditDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

// 스펙 상세 화면 상단 요약 카드를 눌렀을 때 진입하는, 모든 필드(카테고리/제목/기관/취득일 포함)를
// 수정할 수 있는 화면. UI는 SpecAddScreen과 동일하되 기존 값으로 초기화되고, 저장 시 updateSpec 호출.
@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SpecFullEditScreen(
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
    var showDatePicker by remember { mutableStateOf(false) }
    val errorMessage by viewModel.errorMessage.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 12.dp)) {
            IconButton(onClick = onBack, modifier = Modifier.align(Alignment.CenterStart)) {
                Icon(Icons.Default.ArrowBack, contentDescription = "뒤로", tint = CatchTextTitle)
            }
            Text(
                text = "스펙 수정",
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
            SpecTextField(label = "기관 주최", value = organization, onValueChange = { organization = it }, placeholder = "예: 한국산업인력공단")

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    Text(text = "취득일", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = CatchTextTitle)
                }
                Box(modifier = Modifier.weight(1f)) {
                    Text(text = "점수 등급", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = CatchTextTitle)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    Box {
                        OutlinedTextField(
                            value = specDate,
                            onValueChange = {},
                            readOnly = true,
                            placeholder = { Text("2026-07-30", color = CatchTextCaption) },
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Default.CalendarToday,
                                    contentDescription = "날짜 선택",
                                    tint = CatchTextCaption,
                                    modifier = Modifier.padding(end = 4.dp)
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = CatchPrimary,
                                unfocusedBorderColor = CatchInactive
                            )
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .clickable { showDatePicker = true }
                        )
                    }
                }
                Box(modifier = Modifier.weight(1f)) {
                    OutlinedTextField(
                        value = scoreOrGrade,
                        onValueChange = { scoreOrGrade = it },
                        placeholder = { Text("선택", color = CatchTextCaption) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CatchPrimary,
                            unfocusedBorderColor = CatchInactive
                        )
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            SpecTextField(label = "메모 (선택, 최대 200자)", value = memo, onValueChange = { if (it.length <= 200) memo = it }, placeholder = "메모를 입력해주세요 (선택)")

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .height(56.dp)
                    .background(color = CatchPrimary, shape = RoundedCornerShape(12.dp))
                    .clickable {
                        if (title.isNotBlank() && specDate.isNotBlank()) {
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
                    }
            ) {
                Text("저장하기", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            if (errorMessage != null) {
                Text(
                    text = errorMessage!!,
                    fontSize = 13.sp,
                    color = Color(0xFFE05353),
                    modifier = Modifier.padding(top = 12.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val date = Instant.ofEpochMilli(millis).atZone(ZoneId.of("UTC")).toLocalDate()
                        specDate = date.format(specEditDateFormatter)
                    }
                    showDatePicker = false
                }) {
                    Text("확인")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("취소")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}