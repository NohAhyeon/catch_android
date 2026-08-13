package com.umc.catchandroid.presentation.notification

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.umc.catchandroid.R
import com.umc.catchandroid.domain.model.Notification
import com.umc.catchandroid.ui.theme.CatchDeadlineSoon
import com.umc.catchandroid.ui.theme.CatchPrimary
import com.umc.catchandroid.ui.theme.CatchSecondaryLight
import com.umc.catchandroid.ui.theme.CatchTextCaption
import com.umc.catchandroid.ui.theme.CatchTextTitle
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private val filterCategories = listOf("전체", "공지", "마감임박", "키워드")

@Composable
fun NotificationScreen(
    onBack: () -> Unit,
    onNoticeClick: (Long) -> Unit,
    viewModel: NotificationViewModel = hiltViewModel()
) {
    val notifications by viewModel.notifications.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isLoadingMore by viewModel.isLoadingMore.collectAsState()
    val hasMore by viewModel.hasMore.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    var selectedCategory by remember { mutableStateOf(filterCategories.first()) }

    LaunchedEffect(Unit) {
        viewModel.loadNotifications()
    }

    val filteredNotifications = if (selectedCategory == "전체") {
        notifications
    } else {
        notifications.filter { categoryOf(it.notificationType) == selectedCategory }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // 상단 앱바
        Box(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "뒤로가기",
                    tint = CatchTextTitle
                )
            }
            Text(
                text = "알림",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = CatchTextTitle,
                modifier = Modifier.align(Alignment.Center)
            )
            Text(
                text = "모두 읽음",
                fontSize = 13.sp,
                color = CatchTextCaption,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .clickable { viewModel.markAllAsRead() }
            )
        }

        // 카테고리 필터 칩
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            filterCategories.forEach { category ->
                FilterChip(
                    text = category,
                    selected = selectedCategory == category,
                    onClick = { selectedCategory = category }
                )
            }
        }

        when {
            isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = CatchPrimary)
                }
            }
            errorMessage != null -> {
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
                                .clickable { viewModel.loadNotifications() }
                                .padding(horizontal = 20.dp, vertical = 10.dp)
                        ) {
                            Text(text = "다시 시도", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
            filteredNotifications.isEmpty() -> {
                EmptyNotificationState()
            }
            else -> {
                val grouped = filteredNotifications.groupBy { dateGroupLabel(it.createdAt) }

                // 알림이 있을 때는 화면 우측 하단에 인사하는 펭귄 마스코트를 고정 오버레이로 표시
                Box(modifier = Modifier.fillMaxSize()) {
                    LazyColumn(modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp)) {
                        grouped.forEach { (dateLabel, items) ->
                            item {
                                Text(
                                    text = dateLabel,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CatchTextTitle,
                                    modifier = Modifier.padding(top = 8.dp, bottom = 10.dp)
                                )
                            }
                            items(items) { notification ->
                                NotificationItem(
                                    notification = notification,
                                    onClick = { onNoticeClick(notification.noticeId) }
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                            }
                        }

                        // 이전 알림 더 불러오기 (그라데이션 배경 카드)
                        if (hasMore) {
                            item {
                                Spacer(modifier = Modifier.height(8.dp))
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(16.dp))
                                        .clickable(enabled = !isLoadingMore) { viewModel.loadMore() }
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.bg_noti_card),
                                        contentDescription = null,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.matchParentSize()
                                    )
                                    Column(
                                        modifier = Modifier.fillMaxWidth().padding(vertical = 28.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        if (isLoadingMore) {
                                            CircularProgressIndicator(
                                                color = CatchPrimary,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        } else {
                                            Image(
                                                painter = painterResource(id = R.drawable.img_bell),
                                                contentDescription = null,
                                                modifier = Modifier.size(40.dp)
                                            )
                                            Spacer(modifier = Modifier.height(10.dp))
                                            Text(
                                                text = "지난 알림을 확인해 보세요",
                                                fontSize = 13.sp,
                                                color = CatchTextCaption
                                            )
                                            Text(
                                                text = "이전 알림 보기 >",
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = CatchPrimary,
                                                modifier = Modifier.padding(top = 6.dp)
                                            )
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(20.dp))
                            }
                        }
                    }

                    // 우측 하단 고정 펭귄 마스코트 (알림 목록이 있을 때만 표시)
                    Image(
                        painter = painterResource(id = R.drawable.img_penguin),
                        contentDescription = null,
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(end = 16.dp, bottom = 12.dp)
                            .size(150.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyNotificationState() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.img_penguin_empty),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(140.dp)
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "새로운 알림이 없어요",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = CatchTextTitle
            )
            Text(
                text = "중요한 알림이 도착하면\n여기에서 확인할 수 있어요!",
                fontSize = 13.sp,
                color = CatchTextCaption,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
private fun FilterChip(text: String, selected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .background(
                color = if (selected) CatchPrimary else Color.White,
                shape = RoundedCornerShape(50)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = text,
            fontSize = 13.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
            color = if (selected) Color.White else CatchTextCaption
        )
    }
}

@Composable
private fun NotificationItem(notification: Notification, onClick: () -> Unit) {
    val category = categoryOf(notification.notificationType)
    val badgeBg = if (category == "마감임박") CatchDeadlineSoon else CatchSecondaryLight
    val badgeTextColor = if (category == "마감임박") Color.White else CatchPrimary
    val badgeLabel = if (category == "마감임박") "마감 임박" else category

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (notification.isRead) Color.White else CatchSecondaryLight
        ),
        onClick = onClick
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (!notification.isRead) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .background(CatchPrimary, CircleShape)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                    }
                    Box(
                        modifier = Modifier
                            .background(badgeBg, RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(text = badgeLabel, fontSize = 11.sp, color = badgeTextColor)
                    }
                }
                Text(
                    text = relativeTimeLabel(notification.createdAt),
                    fontSize = 11.sp,
                    color = CatchTextCaption
                )
            }
            Text(
                text = notification.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = CatchTextTitle,
                modifier = Modifier.padding(top = 8.dp)
            )
            if (notification.message.isNotBlank()) {
                Text(
                    text = notification.message,
                    fontSize = 13.sp,
                    color = CatchTextCaption,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

// Swagger 응답 확인 결과 notificationType은 "CLOSING"(마감임박), "KEYWORD"(키워드),
// 그 외는 일반 공지로 내려옴
private fun categoryOf(notificationType: String): String {
    val type = notificationType.uppercase()
    return when {
        type == "CLOSING" -> "마감임박"
        type == "KEYWORD" -> "키워드"
        else -> "공지"
    }
}

private fun dateGroupLabel(createdAt: String): String {
    return try {
        val date = LocalDate.parse(createdAt.substring(0, 10), DateTimeFormatter.ISO_DATE)
        val today = LocalDate.now()
        when (date) {
            today -> "오늘"
            today.minusDays(1) -> "어제"
            else -> date.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))
        }
    } catch (e: Exception) {
        ""
    }
}

private fun relativeTimeLabel(createdAt: String): String {
    return try {
        val dateTime = LocalDateTime.parse(createdAt, DateTimeFormatter.ISO_DATE_TIME)
        val today = LocalDate.now()
        when (dateTime.toLocalDate()) {
            today -> dateTime.format(DateTimeFormatter.ofPattern("HH:mm"))
            today.minusDays(1) -> "어제"
            else -> dateTime.toLocalDate().format(DateTimeFormatter.ofPattern("MM.dd"))
        }
    } catch (e: Exception) {
        ""
    }
}