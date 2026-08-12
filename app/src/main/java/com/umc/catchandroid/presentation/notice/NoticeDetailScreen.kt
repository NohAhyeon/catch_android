package com.umc.catchandroid.presentation.notice

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.umc.catchandroid.ui.theme.CatchPrimary
import com.umc.catchandroid.ui.theme.CatchSecondaryLight
import com.umc.catchandroid.ui.theme.CatchTextBody
import com.umc.catchandroid.ui.theme.CatchTextCaption
import com.umc.catchandroid.ui.theme.CatchTextTitle
import androidx.compose.foundation.layout.width
import android.content.Intent

@Composable
fun NoticeDetailScreen(
    noticeId: Long,
    onBack: () -> Unit,
    onOpenOriginal: (String) -> Unit,
    viewModel: NoticeDetailViewModel = hiltViewModel()
) {
    val detail by viewModel.detail.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(noticeId) {
        viewModel.load(noticeId)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // 상단 앱바
        Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 12.dp)) {
            IconButton(onClick = onBack, modifier = Modifier.align(Alignment.CenterStart)) {
                Icon(Icons.Default.ArrowBack, contentDescription = "뒤로가기", tint = CatchTextTitle)
            }
            Text(
                text = "공지 상세",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = CatchTextTitle,
                modifier = Modifier.align(Alignment.Center)
            )
            Row(modifier = Modifier.align(Alignment.CenterEnd)) {
                val notice = detail
                IconButton(onClick = { viewModel.toggleScrap() }) {
                    Icon(
                        imageVector = if (notice?.isScrapped == true) Icons.Default.Star else Icons.Outlined.StarOutline,
                        contentDescription = "스크랩",
                        tint = CatchPrimary
                    )
                }
                IconButton(onClick = {
                    notice?.let {
                        val shareIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, "${it.title}\n${it.originalUrl}")
                        }
                        context.startActivity(Intent.createChooser(shareIntent, "공유하기"))
                    }
                }) {
                    Icon(Icons.Default.Share, contentDescription = "공유하기", tint = CatchTextTitle)
                }
            }
        }

        val notice = detail
        if (notice == null) {
            if (errorMessage != null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = errorMessage ?: "",
                            fontSize = 14.sp,
                            color = CatchTextCaption
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Box(
                            modifier = Modifier
                                .background(CatchPrimary, RoundedCornerShape(10.dp))
                                .clickable { viewModel.retry() }
                                .padding(horizontal = 20.dp, vertical = 10.dp)
                        ) {
                            Text(text = "다시 시도", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = CatchPrimary)
                }
            }
            return
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .background(CatchSecondaryLight, RoundedCornerShape(6.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(text = notice.categoryTag, fontSize = 12.sp, color = CatchPrimary)
                }
                notice.deadlineAt?.let {
                    Box(
                        modifier = Modifier
                            .background(CatchPrimary, RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(text = "마감 ${it.substring(0, 10)}", fontSize = 11.sp, color = Color.White)
                    }
                }
            }

            Text(
                text = notice.title,
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold,
                color = CatchTextTitle,
                modifier = Modifier.padding(top = 12.dp, bottom = 4.dp)
            )
            Text(
                text = "${notice.source} · ${notice.createdAt.substring(0, 10)}",
                fontSize = 12.sp,
                color = CatchTextCaption
            )

            // AI 요약 카드 - aiSummary가 null이어도 카드는 보여주고 준비중 문구 표시
            Card(
                modifier = Modifier.fillMaxWidth().padding(top = 20.dp),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = CatchSecondaryLight)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = CatchPrimary, modifier = Modifier.height(16.dp))
                        Text(
                            text = "AI 3줄 요약",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = CatchPrimary,
                            modifier = Modifier.padding(start = 6.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    val summary = notice.aiSummary
                    if (summary != null) {
                        SummaryRow(label = "지원 자격", value = summary.eligibility)
                        SummaryRow(label = "혜택", value = summary.benefit)
                        SummaryRow(label = "마감", value = summary.deadline)
                    } else {
                        Text(
                            text = "AI 요약을 준비 중이에요",
                            fontSize = 12.sp,
                            color = CatchTextCaption
                        )
                    }
                }
            }

            // 공지 내용
            Text(
                text = "공지 내용",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = CatchTextTitle,
                modifier = Modifier.padding(top = 24.dp, bottom = 8.dp)
            )
            Text(
                text = notice.content ?: "아직 등록된 공지 내용이 없습니다.",
                fontSize = 14.sp,
                color = CatchTextBody,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(28.dp))

            // 원문에서 보기 버튼 - 위에서 아래로 점점 진해지는 세로 그라데이션
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(CatchSecondaryLight, CatchPrimary)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .clickable { onOpenOriginal(notice.originalUrl) },
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "원문에서 보기",
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.height(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun SummaryRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp)) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = CatchTextCaption,
            modifier = Modifier.width(60.dp)
        )
        Text(
            text = value,
            fontSize = 12.sp,
            color = CatchTextBody
        )
    }
}