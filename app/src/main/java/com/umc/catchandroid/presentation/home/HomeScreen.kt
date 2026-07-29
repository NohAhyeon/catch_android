package com.umc.catchandroid.presentation.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.umc.catchandroid.R
import com.umc.catchandroid.domain.model.Notice
import com.umc.catchandroid.ui.theme.CatchDeadlineSoon
import com.umc.catchandroid.ui.theme.CatchInactive
import com.umc.catchandroid.ui.theme.CatchPrimary
import com.umc.catchandroid.ui.theme.CatchSecondaryLight
import com.umc.catchandroid.ui.theme.CatchTextBody
import com.umc.catchandroid.ui.theme.CatchTextCaption
import com.umc.catchandroid.ui.theme.CatchTextTitle
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private val categories = listOf("전체", "장학", "비교과", "학사", "취업")

@Composable
fun HomeScreen(
    onNoticeClick: (Long) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val notices by viewModel.notices.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()
    var selectedCategory by remember { mutableStateOf(categories.first()) }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = CatchPrimary)
        }
        return
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // 상단 앱바
        Box(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_notice2),
                contentDescription = "공지",
                contentScale = androidx.compose.ui.layout.ContentScale.Fit,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .size(50.dp)
            )
            Text(
                text = "공지캐치",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = CatchTextTitle,
                modifier = Modifier.align(Alignment.Center)
            )
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "알림",
                tint = CatchPrimary,
                modifier = Modifier.align(Alignment.CenterEnd)
            )
        }

        LazyColumn(modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp)) {
            item {
                Text(
                    text = "안녕하세요, ${userProfile?.nickname ?: ""}님",
                    fontSize = 14.sp,
                    color = CatchTextBody
                )
                Text(
                    text = "오늘 마감 공지 ${notices.count { it.deadlineAt != null }}개",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = CatchTextTitle,
                    modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
                )
            }

            // 카테고리 필터 칩
            item {
                LazyRow(modifier = Modifier.padding(bottom = 16.dp)) {
                    items(categories) { category ->
                        FilterChip(
                            selected = selectedCategory == category,
                            onClick = { selectedCategory = category },
                            label = { Text(category) },
                            modifier = Modifier.padding(end = 8.dp),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = CatchPrimary,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
            }

            // 오늘 마감 공지 가로 스크롤 카드
            item {
                Text(
                    text = "오늘 마감 공지",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = CatchTextTitle,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CatchSecondaryLight)
                ) {
                    LazyRow(modifier = Modifier.padding(12.dp)) {
                        items(notices.take(3)) { notice ->
                            Card(
                                modifier = Modifier
                                    .width(120.dp)
                                    .padding(end = 8.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White)
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    DDayBadge(dDay = calculateDDay(notice.deadlineAt))
                                    Text(
                                        text = notice.title,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        maxLines = 2,
                                        color = CatchTextTitle,
                                        modifier = Modifier.padding(top = 6.dp)
                                    )
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }

            // 공지 리스트
            items(notices) { notice ->
                NoticeItem(
                    notice = notice,
                    onClick = {
                        viewModel.markAsRead(notice.noticeId)
                        onNoticeClick(notice.noticeId)
                    }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun NoticeItem(notice: Notice, onClick: () -> Unit) {
    val textColor = if (notice.isRead) CatchTextCaption else CatchTextTitle
    val badgeBg = if (notice.isRead) CatchInactive else CatchSecondaryLight
    val badgeText = if (notice.isRead) CatchTextCaption else CatchPrimary
    val ddayBg = if (notice.isRead) CatchInactive else CatchDeadlineSoon
    val ddayText = if (notice.isRead) CatchTextCaption else Color.White

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, CatchInactive),
        onClick = onClick
    ) {
        Row {
            // 왼쪽 색상 라인
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .fillMaxHeight()
                    .background(if (notice.isRead) CatchInactive else CatchPrimary)
            )
            Column(modifier = Modifier.padding(14.dp).weight(1f)) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .background(badgeBg, RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(text = notice.categoryTag, fontSize = 11.sp, color = badgeText)
                    }
                    Box(
                        modifier = Modifier
                            .background(ddayBg, RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(text = calculateDDay(notice.deadlineAt), fontSize = 11.sp, color = ddayText)
                    }
                }
                Text(
                    text = notice.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor,
                    modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                )
                Text(
                    text = notice.source,
                    fontSize = 12.sp,
                    color = CatchTextCaption
                )
            }
        }
    }
}

@Composable
fun DDayBadge(dDay: String, dimmed: Boolean = false) {
    Box(
        modifier = Modifier
            .background(
                color = if (dimmed) CatchInactive else CatchDeadlineSoon,
                shape = RoundedCornerShape(6.dp)
            )
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(text = dDay, fontSize = 11.sp, color = if (dimmed) CatchTextCaption else Color.White)
    }
}

private fun calculateDDay(deadlineAt: String?): String {
    if (deadlineAt == null) return "상시"
    return try {
        val deadlineDate = LocalDate.parse(deadlineAt.substring(0, 10), DateTimeFormatter.ISO_DATE)
        val today = LocalDate.of(2026, 7, 13)
        val diff = java.time.temporal.ChronoUnit.DAYS.between(today, deadlineDate)
        if (diff <= 0) "D-Day" else "D-$diff"
    } catch (e: Exception) {
        ""
    }
}