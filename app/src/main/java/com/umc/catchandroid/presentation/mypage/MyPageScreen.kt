package com.umc.catchandroid.presentation.mypage

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.umc.catchandroid.R
import com.umc.catchandroid.ui.theme.CatchPrimary
import com.umc.catchandroid.ui.theme.CatchSecondaryLight
import com.umc.catchandroid.ui.theme.CatchTextBody
import com.umc.catchandroid.ui.theme.CatchTextCaption
import com.umc.catchandroid.ui.theme.CatchTextTitle
import androidx.compose.ui.draw.clip


@Composable
fun MyPageScreen(
    onLogout: () -> Unit,
    onKeywordManageClick: () -> Unit = {},
    onSchoolInfoClick: () -> Unit = {},
    onNotificationSettingsClick: () -> Unit = {},
    onSpecLogClick: () -> Unit = {},
    viewModel: MyPageViewModel = hiltViewModel()
) {
    val profile by viewModel.userProfile.collectAsState()
    var showLogoutDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.refreshProfile()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // 상단 앱바
        Box(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Text(
                text = "마이페이지",
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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            profile?.let { user ->
                Text(
                    text = "안녕하세요, ${user.nickname}님!",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = CatchTextTitle
                )
                Text(
                    text = "오늘도 원하는 공지를 놓치지 않도록 도와드릴게요.",
                    fontSize = 13.sp,
                    color = CatchTextCaption,
                    modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
                )

                // 프로필 카드
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CatchSecondaryLight)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.character2),
                            contentDescription = "프로필 사진",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                        )
                        Column(modifier = Modifier.weight(1f).padding(start = 12.dp)) {
                            Text(
                                text = user.nickname,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = CatchTextTitle
                            )
                            Text(
                                text = "${user.universityName}\n${user.departmentName} · ${user.grade}학년",
                                fontSize = 12.sp,
                                color = CatchTextCaption,
                                lineHeight = 16.sp
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = "프로필 상세",
                            tint = CatchTextCaption
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 통계 3개 - 카드 스타일
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    StatCard(count = user.scrapCount, label = "스크랩", modifier = Modifier.weight(1f))
                    StatCard(count = user.keywordCount, label = "관심키워드", modifier = Modifier.weight(1f))
                    StatCard(count = user.readCount, label = "스펙로그", modifier = Modifier.weight(1f))
                }

                Spacer(modifier = Modifier.height(24.dp))

                // 메뉴 리스트
                MenuRow(icon = Icons.Default.Star, label = "스크랩")
                MenuRow(icon = Icons.Default.Assignment, label = "스펙 로그", onClick = onSpecLogClick)
                MenuRow(icon = Icons.Default.Bookmark, label = "관심 키워드", onClick = onKeywordManageClick)
                MenuRow(icon = Icons.Default.Notifications, label = "알림 설정", onClick = onNotificationSettingsClick)
                MenuRow(icon = Icons.Default.School, label = "학교 정보 수정", onClick = onSchoolInfoClick)
                MenuRow(icon = Icons.Default.Info, label = "공지사항 · FAQ")
                MenuRow(icon = Icons.Default.HelpOutline, label = "문의하기")

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "로그아웃",
                    fontSize = 14.sp,
                    color = Color(0xFFE05353),
                    modifier = Modifier
                        .clickable { showLogoutDialog = true }
                        .padding(vertical = 8.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))
            } ?: Text("로딩 중...", color = CatchTextCaption)
        }
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = {
                Text(
                    text = "로그아웃 하시겠어요?",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = CatchTextTitle
                )
            },
            text = {
                Text(
                    text = "다시 로그인해야 이용할 수 있어요",
                    fontSize = 13.sp,
                    color = CatchTextCaption
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showLogoutDialog = false
                        viewModel.logout(onComplete = onLogout)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CatchPrimary)
                ) {
                    Text("로그아웃", color = Color.White)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showLogoutDialog = false }) {
                    Text("취소", color = CatchTextBody)
                }
            }
        )
    }
}

@Composable
private fun StatCard(count: Int, label: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = count.toString(),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = CatchTextTitle
            )
            Text(
                text = label,
                fontSize = 12.sp,
                color = CatchTextCaption
            )
        }
    }
}

@Composable
private fun MenuRow(icon: ImageVector, label: String, onClick: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = CatchPrimary,
            modifier = Modifier.size(22.dp)
        )
        Text(
            text = label,
            fontSize = 14.sp,
            color = CatchTextTitle,
            modifier = Modifier.padding(start = 12.dp)
        )
    }
}