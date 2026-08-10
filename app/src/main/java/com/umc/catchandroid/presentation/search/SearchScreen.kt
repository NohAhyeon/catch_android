package com.umc.catchandroid.presentation.search

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.umc.catchandroid.presentation.component.NotificationBellIcon
import com.umc.catchandroid.presentation.home.NoticeItem
import com.umc.catchandroid.ui.theme.CatchDivider
import com.umc.catchandroid.ui.theme.CatchPrimary
import com.umc.catchandroid.ui.theme.CatchSecondaryLight
import com.umc.catchandroid.ui.theme.CatchTextCaption
import com.umc.catchandroid.ui.theme.CatchTextTitle
import androidx.compose.foundation.layout.width

@Composable
fun SearchScreen(
    onNoticeClick: (Long) -> Unit,
    onNotificationClick: () -> Unit = {},
    hasUnreadNotification: Boolean = false,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val keyword by viewModel.keyword.collectAsState()
    val results by viewModel.results.collectAsState()
    val recentSearches by viewModel.recentSearches.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        // 상단 앱바
        Box(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Text(
                text = "검색",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = CatchTextTitle,
                modifier = Modifier.align(Alignment.Center)
            )
            NotificationBellIcon(
                hasUnread = hasUnreadNotification,
                onClick = onNotificationClick,
                modifier = Modifier.align(Alignment.CenterEnd)
            )
        }

        LazyColumn(modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp)) {
            // 검색창
            item {
                OutlinedTextField(
                    value = keyword,
                    onValueChange = { viewModel.onKeywordChange(it) },
                    placeholder = { Text("공지 검색 (제목·본문·카테고리)", color = CatchTextCaption) },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "검색",
                            tint = CatchPrimary,
                            modifier = Modifier
                                .padding(end = 4.dp)
                                .clickable { viewModel.search() }
                        )
                    },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CatchPrimary,
                        unfocusedBorderColor = CatchDivider
                    )
                )
            }

            // 최근 검색어
            if (recentSearches.isNotEmpty()) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "최근 검색어",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = CatchTextTitle
                        )
                        Text(
                            text = "전체 삭제",
                            fontSize = 13.sp,
                            color = CatchPrimary,
                            modifier = Modifier.clickable { viewModel.clearRecentSearches() }
                        )
                    }
                }
                item {
                    LazyRow(modifier = Modifier.padding(bottom = 24.dp)) {
                        items(recentSearches) { term ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .background(CatchSecondaryLight, RoundedCornerShape(20.dp))
                                    .padding(horizontal = 12.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = term,
                                    fontSize = 13.sp,
                                    color = CatchPrimary,
                                    modifier = Modifier
                                        .padding(end = 6.dp)
                                        .clickable { viewModel.search(term) }
                                )
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "삭제",
                                    tint = CatchPrimary,
                                    modifier = Modifier
                                        .height(14.dp)
                                        .clickable { viewModel.removeRecentSearch(term) }
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                    }
                }
            }

            // 검색 결과
            item {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "검색 결과",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = CatchTextTitle
                    )
                    Text(
                        text = "최신순",
                        fontSize = 13.sp,
                        color = CatchTextCaption
                    )
                }
            }

            items(results) { notice ->
                NoticeItem(notice = notice, onClick = { onNoticeClick(notice.noticeId) })
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}