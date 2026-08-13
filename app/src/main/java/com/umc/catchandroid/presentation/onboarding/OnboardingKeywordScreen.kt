package com.umc.catchandroid.presentation.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.umc.catchandroid.R
import com.umc.catchandroid.ui.theme.CatchInactive
import com.umc.catchandroid.ui.theme.CatchPrimary
import com.umc.catchandroid.ui.theme.CatchSecondary
import com.umc.catchandroid.ui.theme.CatchTextCaption
import com.umc.catchandroid.ui.theme.CatchTextTitle

private val recommendedKeywords = listOf("장학금", "비교과", "학사", "취업")

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun OnboardingKeywordScreen(
    onComplete: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    var selectedRecommended by remember { mutableStateOf(setOf("장학금", "비교과", "취업")) }
    var customKeywords by remember { mutableStateOf(setOf<String>()) }
    var customInput by remember { mutableStateOf("") }

    val totalCount = selectedRecommended.size + customKeywords.size

    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp)) {
        Text(
            text = "관심 키워드",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = CatchTextTitle,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth().padding(top = 20.dp, bottom = 16.dp)
        )
        HorizontalDivider(color = CatchInactive, thickness = 1.dp)

        Text(
            text = "관심사를 고르면 맞춤 공지를 받아요 ($totalCount/4)",
            fontSize = 13.sp,
            color = CatchTextCaption,
            modifier = Modifier.padding(top = 16.dp, bottom = 40.dp)
        )

        Text(
            text = "추천 키워드",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = CatchTextTitle,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        FlowRow(modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp)) {
            recommendedKeywords.forEach { keyword ->
                val isSelected = selectedRecommended.contains(keyword)
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
                        .clickable {
                            selectedRecommended = if (isSelected) {
                                selectedRecommended - keyword
                            } else {
                                selectedRecommended + keyword
                            }
                        }
                        .padding(horizontal = 20.dp)
                ) {
                    Text(
                        text = keyword,
                        fontSize = 14.sp,
                        color = if (isSelected) Color.White else CatchTextTitle
                    )
                }
            }
        }

        Text(
            text = "직접 입력",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = CatchTextTitle,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        OutlinedTextField(
            value = customInput,
            onValueChange = { customInput = it },
            placeholder = { Text("키워드 입력 후 Enter (2-20자)", color = CatchTextCaption) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    val trimmed = customInput.trim()
                    if (trimmed.length in 2..20) {
                        customKeywords = customKeywords + trimmed
                        customInput = ""
                    }
                }
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = CatchPrimary,
                unfocusedBorderColor = CatchInactive
            )
        )

        if (customKeywords.isNotEmpty()) {
            FlowRow(modifier = Modifier.fillMaxWidth().padding(top = 12.dp)) {
                customKeywords.forEach { keyword ->
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .padding(end = 8.dp, bottom = 8.dp)
                            .height(36.dp)
                            .background(
                                color = CatchSecondary,
                                shape = RoundedCornerShape(18.dp)
                            )
                            .clickable {
                                customKeywords = customKeywords - keyword
                            }
                            .padding(horizontal = 16.dp)
                    ) {
                        Text(text = "$keyword ✕", fontSize = 13.sp, color = Color.White)
                    }
                }
            }
        }

        Column(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Bottom
        ) {
            Image(
                painter = painterResource(id = R.drawable.character),
                contentDescription = "공지캐치 캐릭터",
                contentScale = androidx.compose.ui.layout.ContentScale.FillHeight,
                modifier = Modifier.height(200.dp)
            )
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
                .height(56.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFF9B98FF), Color(0xFF403DE4))
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
                .clickable {
                    viewModel.saveKeywords(
                        recommended = selectedRecommended.toList(),
                        custom = customKeywords.toList(),
                        onDone = onComplete
                    )
                }
        ) {
            Text(
                text = "완료하고 시작하기",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}