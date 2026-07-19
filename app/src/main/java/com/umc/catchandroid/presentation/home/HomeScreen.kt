package com.umc.catchandroid.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.umc.catchandroid.domain.model.Notice
import com.umc.catchandroid.ui.theme.CatchDivider
import com.umc.catchandroid.ui.theme.CatchPrimary
import com.umc.catchandroid.ui.theme.CatchSecondaryLight
import com.umc.catchandroid.ui.theme.CatchTextBody
import com.umc.catchandroid.ui.theme.CatchTextCaption
import com.umc.catchandroid.ui.theme.CatchTextTitle
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

private val categories = listOf("장학금", "장학", "비교과", "학사", "취업")

@Composable
fun HomeScreen(
    onNoticeClick: (Long) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val notices by viewModel.notices.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    var selectedCategory by remember { mutableStateOf(categories.first()) }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = CatchPrimary)
        }
        return
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
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
    val bgColor = if (notice.isRead) CatchDivider.copy(alpha = 0.15f) else Color.White
    val textColor = if (notice.isRead) CatchTextCaption else CatchTextTitle

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        onClick = onClick
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                CategoryTag(text = notice.categoryTag, dimmed = notice.isRead)
                DDayBadge(dDay = calculateDDay(notice.deadlineAt), dimmed = notice.isRead)
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

@Composable
fun CategoryTag(text: String, dimmed: Boolean = false) {
    Box(
        modifier = Modifier
            .background(
                color = if (dimmed) CatchDivider.copy(alpha = 0.3f) else CatchSecondaryLight,
                shape = RoundedCornerShape(6.dp)
            )
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(text = text, fontSize = 11.sp, color = if (dimmed) CatchTextCaption else CatchPrimary)
    }
}

@Composable
fun DDayBadge(dDay: String, dimmed: Boolean = false) {
    Box(
        modifier = Modifier
            .background(
                color = if (dimmed) CatchDivider.copy(alpha = 0.3f) else CatchPrimary,
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
        val diff = ChronoUnit.DAYS.between(today, deadlineDate)
        if (diff <= 0) "D-Day" else "D-$diff"
    } catch (e: Exception) {
        ""
    }
}