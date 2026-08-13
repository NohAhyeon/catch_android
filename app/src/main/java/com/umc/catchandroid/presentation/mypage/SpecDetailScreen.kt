package com.umc.catchandroid.presentation.mypage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.umc.catchandroid.R
import com.umc.catchandroid.domain.model.Spec
import com.umc.catchandroid.ui.theme.CatchPrimary
import com.umc.catchandroid.ui.theme.CatchSecondaryLight
import com.umc.catchandroid.ui.theme.CatchTextCaption
import com.umc.catchandroid.ui.theme.CatchTextTitle


@Composable
fun SpecDetailScreen(
    spec: Spec,
    onBack: () -> Unit,
    onEditFullClick: (Spec) -> Unit = {},
    viewModel: SpecFormViewModel = hiltViewModel()
) {
    // 카테고리/제목/기관/취득일은 요약 카드로만 보여주고 수정 불가 (Figma 기준)
    // 점수 등급과 메모만 수정 가능
    var scoreOrGrade by remember { mutableStateOf(spec.scoreOrGrade ?: "") }
    var memo by remember { mutableStateOf(spec.memo ?: "") }
    var showDeleteDialog by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
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
                // 요약 카드 (카테고리/날짜/제목/기관 - 수정 불가)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, bottom = 24.dp)
                        .clickable { onEditFullClick(spec) },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Box(
                                    modifier = Modifier
                                        .background(CatchSecondaryLight, RoundedCornerShape(6.dp))
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = spec.categoryTag.ifEmpty { specCategoryLabel(spec.category) },
                                        fontSize = 11.sp,
                                        color = CatchPrimary
                                    )
                                }
                                Text(text = spec.specDate, fontSize = 12.sp, color = CatchTextCaption)
                            }
                            Text(
                                text = spec.title,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = CatchTextTitle,
                                modifier = Modifier.padding(top = 10.dp)
                            )
                            Text(
                                text = spec.organization,
                                fontSize = 12.sp,
                                color = CatchTextCaption,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }

                SpecTextField(
                    label = "점수 등급",
                    value = scoreOrGrade,
                    onValueChange = { scoreOrGrade = it },
                    placeholder = "예: 합격"
                )
                SpecTextField(
                    label = "메모 (선택, 최대 200자)",
                    value = memo,
                    onValueChange = { if (it.length <= 200) memo = it },
                    placeholder = "메모를 입력해주세요 (선택)"
                )

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                        .height(56.dp)
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(Color(0xFF9B98FF), Color(0xFF403DE4))
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clickable {
                            viewModel.updateSpec(
                                spec.specId,
                                Spec(
                                    specId = spec.specId,
                                    category = spec.category,
                                    categoryTag = spec.categoryTag,
                                    title = spec.title,
                                    organization = spec.organization,
                                    specDate = spec.specDate,
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

        Image(
            painter = painterResource(id = R.drawable.ic_spec),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 20.dp, bottom = 20.dp)
                .size(150.dp)
        )
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