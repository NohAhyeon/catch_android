package com.umc.catchandroid.presentation.mypage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.umc.catchandroid.R
import com.umc.catchandroid.domain.model.Spec
import com.umc.catchandroid.domain.model.SpecCategoryCounts
import com.umc.catchandroid.ui.theme.CatchInactive
import com.umc.catchandroid.ui.theme.CatchPrimary
import com.umc.catchandroid.ui.theme.CatchSecondaryLight
import com.umc.catchandroid.ui.theme.CatchTextCaption
import com.umc.catchandroid.ui.theme.CatchTextTitle

private val filterCategories = listOf("ALL" to "전체") + specCategories

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SpecLogScreen(
    onBack: () -> Unit,
    onAddClick: () -> Unit,
    onSpecClick: (Spec) -> Unit,
    viewModel: SpecLogViewModel = hiltViewModel()
) {
    val specs by viewModel.specs.collectAsState()
    val categoryCounts by viewModel.categoryCounts.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 12.dp)) {
                IconButton(onClick = onBack, modifier = Modifier.align(Alignment.CenterStart)) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "뒤로", tint = CatchTextTitle)
                }
                Text(
                    text = "스펙 로그",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = CatchTextTitle,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            Column(modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp)) {
                Text(
                    text = "카테고리",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = CatchTextTitle,
                    modifier = Modifier.padding(top = 8.dp, bottom = 12.dp)
                )
                FlowRow(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
                    filterCategories.forEach { (code, label) ->
                        val isSelected = selectedCategory == code
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .padding(end = 8.dp, bottom = 8.dp)
                                .height(36.dp)
                                .background(
                                    color = if (isSelected) CatchPrimary else CatchInactive,
                                    shape = RoundedCornerShape(18.dp)
                                )
                                .clickable { viewModel.selectCategory(code) }
                                .padding(horizontal = 16.dp)
                        ) {
                            Text(
                                text = "$label ${countFor(code, categoryCounts)}",
                                fontSize = 13.sp,
                                color = if (isSelected) Color.White else CatchTextTitle
                            )
                        }
                    }
                }

                if (!isLoading && specs.isEmpty()) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_skill),
                            contentDescription = "스펙 없음",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.size(120.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "저장된 스펙 로그가 없어요",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = CatchTextTitle
                        )
                        Text(
                            text = "첫 스펙을 등록하고 성장 기록을 관리해 보세요!",
                            fontSize = 13.sp,
                            color = CatchTextCaption,
                            modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
                        )
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .background(color = CatchPrimary, shape = RoundedCornerShape(12.dp))
                                .clickable { onAddClick() }
                        ) {
                            Text("+ 스펙 로그 추가하기", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                } else {
                    LazyColumn(modifier = Modifier.weight(1f)) {
                        items(specs) { spec ->
                            SpecCard(spec = spec, onClick = { onSpecClick(spec) })
                        }
                    }
                }
            }
        }

        if (specs.isNotEmpty()) {
            FloatingActionButton(
                onClick = onAddClick,
                containerColor = CatchPrimary,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(20.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "스펙 추가", tint = Color.White)
            }
        }
    }
}

private fun countFor(code: String, counts: SpecCategoryCounts?): Int {
    if (counts == null) return 0
    return when (code) {
        "ALL" -> counts.allCount
        "LICENSE" -> counts.licenseCount
        "AWARD" -> counts.awardCount
        "ACTIVITY" -> counts.activityCount
        "LANGUAGE" -> counts.languageCount
        "INTERN" -> counts.internCount
        "ETC" -> counts.etcCount
        else -> 0
    }
}

@Composable
private fun SpecCard(spec: Spec, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .background(color = CatchSecondaryLight, shape = RoundedCornerShape(6.dp))
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
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = CatchTextTitle,
                modifier = Modifier.padding(top = 8.dp)
            )
            Text(
                text = spec.organization,
                fontSize = 12.sp,
                color = CatchTextCaption,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}