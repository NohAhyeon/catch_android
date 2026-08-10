package com.umc.catchandroid.presentation.mypage

import androidx.compose.animation.animateContentSize
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Headset
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.umc.catchandroid.domain.model.Faq
import com.umc.catchandroid.domain.model.SupportNotice
import com.umc.catchandroid.ui.theme.CatchInactive
import com.umc.catchandroid.ui.theme.CatchPrimary
import com.umc.catchandroid.ui.theme.CatchSecondaryLight
import com.umc.catchandroid.ui.theme.CatchTextCaption
import com.umc.catchandroid.ui.theme.CatchTextTitle
import kotlinx.coroutines.launch

private enum class SupportTab { NOTICE, FAQ }

@Composable
fun SupportNoticeScreen(
    onBack: () -> Unit,
    onContactClick: () -> Unit = {},
    viewModel: SupportViewModel = hiltViewModel()
) {
    val notices by viewModel.notices.collectAsState()
    val faqs by viewModel.faqs.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    var selectedTab by remember { mutableStateOf(SupportTab.NOTICE) }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.load()
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
                text = "공지사항",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = CatchTextTitle,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        // 탭 (스크롤 앵커)
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SupportTabButton(
                text = "공지사항",
                selected = selectedTab == SupportTab.NOTICE,
                modifier = Modifier.weight(1f),
                onClick = {
                    selectedTab = SupportTab.NOTICE
                    coroutineScope.launch { listState.animateScrollToItem(0) }
                }
            )
            SupportTabButton(
                text = "FAQ",
                selected = selectedTab == SupportTab.FAQ,
                modifier = Modifier.weight(1f),
                onClick = {
                    selectedTab = SupportTab.FAQ
                    coroutineScope.launch {
                        // 공지 리스트 개수만큼 지나서 FAQ 섹션 헤더로 스크롤
                        listState.animateScrollToItem(notices.size + 1)
                    }
                }
            )
        }

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = CatchPrimary)
            }
            return
        }

        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp)
        ) {
            item {
                Text(
                    text = "운영팀 공지",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = CatchTextTitle,
                    modifier = Modifier.padding(bottom = 10.dp, top = 8.dp)
                )
            }
            items(notices) { notice ->
                SupportNoticeItem(notice = notice)
                Spacer(modifier = Modifier.height(10.dp))
            }

            item {
                Text(
                    text = "FAQ",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = CatchTextTitle,
                    modifier = Modifier.padding(top = 16.dp, bottom = 10.dp)
                )
            }
            items(faqs) { faq ->
                FaqItem(faq = faq, onContactClick = onContactClick)
                Spacer(modifier = Modifier.height(10.dp))
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }
        }
    }
}

@Composable
private fun SupportTabButton(
    text: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    if (selected) {
        Button(
            onClick = onClick,
            modifier = modifier.height(44.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = CatchPrimary)
        ) {
            Text(text, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }
    } else {
        OutlinedButton(
            onClick = onClick,
            modifier = modifier.height(44.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(text, color = CatchTextTitle, fontSize = 14.sp)
        }
    }
}

@Composable
private fun SupportNoticeItem(notice: SupportNotice) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
            .clickable { expanded = !expanded },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .background(CatchSecondaryLight, RoundedCornerShape(6.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(text = "공지", fontSize = 11.sp, color = CatchPrimary)
                }
                Text(
                    text = notice.createdAt.substring(0, 10).replace("-", "."),
                    fontSize = 12.sp,
                    color = CatchTextCaption
                )
            }
            Text(
                text = notice.title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = CatchTextTitle,
                modifier = Modifier.padding(top = 8.dp)
            )
            if (expanded) {
                Text(
                    text = notice.content,
                    fontSize = 13.sp,
                    color = CatchTextCaption,
                    modifier = Modifier.padding(top = 10.dp)
                )
            }
        }
    }
}

@Composable
private fun FaqItem(faq: Faq, onContactClick: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
            .clickable { expanded = !expanded },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, CatchInactive)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = faq.question,
                    fontSize = 14.sp,
                    color = if (expanded) CatchTextTitle else CatchTextCaption,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = if (expanded) Icons.Default.Remove else Icons.Default.Add,
                    contentDescription = if (expanded) "접기" else "펼치기",
                    tint = CatchTextCaption
                )
            }
            if (expanded) {
                Text(
                    text = faq.answer,
                    fontSize = 13.sp,
                    color = CatchTextCaption,
                    lineHeight = 20.sp,
                    modifier = Modifier.padding(top = 10.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(CatchSecondaryLight, RoundedCornerShape(10.dp))
                        .clickable { onContactClick() }
                        .padding(vertical = 10.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Headset,
                        contentDescription = "문의하기",
                        tint = CatchPrimary,
                        modifier = Modifier.height(16.dp)
                    )
                    Text(
                        text = "추가 도움이 필요하다면 문의하기를 이용해 주세요.",
                        fontSize = 12.sp,
                        color = CatchPrimary,
                        modifier = Modifier.padding(start = 6.dp)
                    )
                }
            }
        }
    }
}