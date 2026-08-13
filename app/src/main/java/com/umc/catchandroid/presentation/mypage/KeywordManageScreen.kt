package com.umc.catchandroid.presentation.mypage

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
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
import com.umc.catchandroid.ui.theme.CatchInactive
import com.umc.catchandroid.ui.theme.CatchPrimary
import com.umc.catchandroid.ui.theme.CatchTextCaption
import com.umc.catchandroid.ui.theme.CatchTextTitle
import androidx.compose.ui.graphics.Brush

private val recommendedKeywords = listOf("장학금", "비교과", "학사", "취업")
private const val MAX_KEYWORDS = 5

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun KeywordManageScreen(
    onBack: () -> Unit,
    viewModel: KeywordManageViewModel = hiltViewModel()
) {
    val selectedRecommended by viewModel.selectedRecommended.collectAsState()
    val customKeywords by viewModel.customKeywords.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    var customInput by remember { mutableStateOf("") }

    val totalCount = selectedRecommended.size + customKeywords.size
    val availableRecommended = recommendedKeywords.filterNot { selectedRecommended.contains(it) }

    Column(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 12.dp)) {
            IconButton(onClick = onBack, modifier = Modifier.align(Alignment.CenterStart)) {
                Icon(Icons.Default.ArrowBack, contentDescription = "뒤로", tint = CatchTextTitle)
            }
            Text(
                text = "관심 키워드 관리",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = CatchTextTitle,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("불러오는 중...", color = CatchTextCaption)
            }
        } else {
            Column(modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp)) {
                Text(
                    text = "현재 키워드 ($totalCount/$MAX_KEYWORDS)",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = CatchTextTitle,
                    modifier = Modifier.padding(top = 16.dp, bottom = 12.dp)
                )

                FlowRow(modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)) {
                    selectedRecommended.forEach { keyword ->
                        KeywordChip(
                            text = keyword,
                            filled = true,
                            onClick = { viewModel.toggleRecommended(keyword) }
                        )
                    }
                    customKeywords.forEach { keyword ->
                        KeywordChip(
                            text = keyword,
                            filled = true,
                            onClick = { viewModel.removeCustomKeyword(keyword) }
                        )
                    }
                }

                Text(
                    text = "키워드 추가",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = CatchTextTitle,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = customInput,
                        onValueChange = { customInput = it },
                        placeholder = { Text("키워드 추가 (추천·직접 입력)", color = CatchTextCaption) },
                        modifier = Modifier.weight(1f),
                        enabled = totalCount < MAX_KEYWORDS,
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CatchPrimary,
                            unfocusedBorderColor = CatchInactive
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .height(56.dp)
                            .background(color = CatchPrimary, shape = RoundedCornerShape(12.dp))
                            .clickable {
                                val trimmed = customInput.trim()
                                if (trimmed.length in 2..20 &&
                                    totalCount < MAX_KEYWORDS &&
                                    !selectedRecommended.contains(trimmed) &&
                                    !customKeywords.contains(trimmed)
                                ) {
                                    viewModel.addCustomKeyword(trimmed)
                                    customInput = ""
                                }
                            }
                            .padding(horizontal = 20.dp)
                    ) {
                        Text("추가", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                }

                if (availableRecommended.isNotEmpty()) {
                    FlowRow(modifier = Modifier.fillMaxWidth().padding(top = 12.dp)) {
                        availableRecommended.forEach { keyword ->
                            KeywordChip(
                                text = keyword,
                                filled = false,
                                onClick = {
                                    if (totalCount < MAX_KEYWORDS) {
                                        viewModel.toggleRecommended(keyword)
                                    }
                                }
                            )
                        }
                    }
                }

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 32.dp)
                        .height(56.dp)
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(Color(0xFF9B98FF), Color(0xFF403DE4))
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clickable { viewModel.save(onDone = onBack) }
                ) {
                    Text(
                        text = "저장하기",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                if (errorMessage != null) {
                    Text(
                        text = errorMessage ?: "",
                        fontSize = 13.sp,
                        color = Color(0xFFE05353),
                        modifier = Modifier.padding(top = 12.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun KeywordChip(text: String, filled: Boolean, onClick: () -> Unit) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(end = 8.dp, bottom = 8.dp)
            .height(40.dp)
            .background(
                color = if (filled) CatchPrimary else Color(0xFFE3E8FD),
                shape = RoundedCornerShape(20.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 20.dp)
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
            color = if (filled) Color.White else CatchTextTitle
        )
    }
}