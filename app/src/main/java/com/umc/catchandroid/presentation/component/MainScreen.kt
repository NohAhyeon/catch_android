package com.umc.catchandroid.presentation.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.umc.catchandroid.presentation.calendar.CalendarScreen
import com.umc.catchandroid.presentation.home.HomeScreen
import com.umc.catchandroid.presentation.mypage.KeywordManageScreen
import com.umc.catchandroid.presentation.mypage.MyPageScreen
import com.umc.catchandroid.presentation.mypage.NotificationSettingsScreen
import com.umc.catchandroid.presentation.mypage.SchoolInfoEditScreen
import com.umc.catchandroid.presentation.mypage.ScrapScreen
import com.umc.catchandroid.presentation.mypage.SpecAddScreen
import com.umc.catchandroid.presentation.mypage.SpecDetailScreen
import com.umc.catchandroid.presentation.mypage.SpecFullEditScreen
import com.umc.catchandroid.presentation.mypage.SpecLogScreen
import com.umc.catchandroid.presentation.mypage.SupportNoticeScreen
import com.umc.catchandroid.presentation.notice.NoticeDetailScreen
import com.umc.catchandroid.presentation.notification.NotificationBadgeViewModel
import com.umc.catchandroid.presentation.notification.NotificationScreen
import com.umc.catchandroid.presentation.search.SearchScreen
import com.umc.catchandroid.presentation.notice.WebViewScreen

@Composable
fun MainScreen(
    onLogout: () -> Unit = {}
) {
    val mainNavController = rememberNavController()
    val notificationBadgeViewModel: NotificationBadgeViewModel = hiltViewModel()
    val hasUnreadNotification by notificationBadgeViewModel.hasUnread.collectAsState()

    val connectivityViewModel: ConnectivityViewModel = hiltViewModel()
    val isOnline by connectivityViewModel.isOnline.collectAsState()

    LaunchedEffect(Unit) {
        notificationBadgeViewModel.refresh()
        notificationBadgeViewModel.registerFcmToken()
    }

    val goToNotification: () -> Unit = {
        notificationBadgeViewModel.clear()
        mainNavController.navigate(Screen.Notification.route)
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        // 실제 네트워크 연결 상태를 직접 반영하는 배너 - 어느 화면에 있든 즉시 뜸
        AnimatedVisibility(
            visible = !isOnline,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFE05353))
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "인터넷 연결이 끊겼어요",
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Scaffold(
            bottomBar = { BottomNavBar(navController = mainNavController) }
        ) { innerPadding ->
            NavHost(
                navController = mainNavController,
                startDestination = Screen.Home.route,
                modifier = androidx.compose.ui.Modifier.padding(innerPadding)
            ) {
                composable(Screen.Home.route) {
                    HomeScreen(
                        onNoticeClick = { noticeId ->
                            mainNavController.navigate(Screen.NoticeDetail.createRoute(noticeId))
                        },
                        onNotificationClick = goToNotification,
                        hasUnreadNotification = hasUnreadNotification
                    )
                }
                composable(Screen.NoticeDetail.route) { backStackEntry ->
                    val noticeId = backStackEntry.arguments
                        ?.getString("noticeId")
                        ?.toLongOrNull() ?: 0L
                    NoticeDetailScreen(
                        noticeId = noticeId,
                        onBack = { mainNavController.popBackStack() },
                        onOpenOriginal = { url ->
                            mainNavController.navigate(Screen.WebView.createRoute(url))
                        }
                    )
                }
                composable(Screen.WebView.route) { backStackEntry ->
                    val encodedUrl = backStackEntry.arguments?.getString("url") ?: ""
                    val url = Screen.WebView.decodeUrl(encodedUrl)
                    WebViewScreen(
                        url = url,
                        onBack = { mainNavController.popBackStack() }
                    )
                }
                composable(Screen.Search.route) {
                    SearchScreen(
                        onNoticeClick = { noticeId ->
                            mainNavController.navigate(Screen.NoticeDetail.createRoute(noticeId))
                        },
                        onNotificationClick = goToNotification,
                        hasUnreadNotification = hasUnreadNotification
                    )
                }
                composable(Screen.Calendar.route) {
                    CalendarScreen(
                        onNoticeClick = { noticeId ->
                            mainNavController.navigate(Screen.NoticeDetail.createRoute(noticeId))
                        },
                        onNotificationClick = goToNotification,
                        hasUnreadNotification = hasUnreadNotification
                    )
                }
                composable(Screen.MyPage.route) {
                    MyPageScreen(
                        onLogout = onLogout,
                        onKeywordManageClick = { mainNavController.navigate(Screen.KeywordManage.route) },
                        onSchoolInfoClick = { mainNavController.navigate(Screen.SchoolInfoEdit.route) },
                        onNotificationSettingsClick = { mainNavController.navigate(Screen.NotificationSettings.route) },
                        onSpecLogClick = { mainNavController.navigate(Screen.SpecLog.route) },
                        onScrapClick = { mainNavController.navigate(Screen.Scrap.route) },
                        onSupportNoticeClick = { mainNavController.navigate(Screen.SupportNotice.route) },
                        onNotificationClick = goToNotification,
                        hasUnreadNotification = hasUnreadNotification
                    )
                }
                composable(Screen.KeywordManage.route) {
                    KeywordManageScreen(
                        onBack = { mainNavController.popBackStack() }
                    )
                }
                composable(Screen.SchoolInfoEdit.route) {
                    SchoolInfoEditScreen(
                        onBack = { mainNavController.popBackStack() }
                    )
                }
                composable(Screen.NotificationSettings.route) {
                    NotificationSettingsScreen(
                        onBack = { mainNavController.popBackStack() }
                    )
                }
                composable(Screen.SpecLog.route) {
                    SpecLogScreen(
                        onBack = { mainNavController.popBackStack() },
                        onAddClick = { mainNavController.navigate(Screen.SpecAdd.route) },
                        onSpecClick = { spec ->
                            mainNavController.navigate(Screen.SpecDetail.createRoute(spec))
                        }
                    )
                }
                composable(Screen.SpecAdd.route) {
                    SpecAddScreen(
                        onBack = { mainNavController.popBackStack() }
                    )
                }
                composable(Screen.SpecDetail.route) { backStackEntry ->
                    val encoded = backStackEntry.arguments?.getString("specData") ?: ""
                    val spec = Screen.SpecDetail.decodeSpec(encoded)
                    SpecDetailScreen(
                        spec = spec,
                        onBack = { mainNavController.popBackStack() },
                        onEditFullClick = { s ->
                            mainNavController.navigate(Screen.SpecFullEdit.createRoute(s))
                        }
                    )
                }
                composable(Screen.SpecFullEdit.route) { backStackEntry ->
                    val encoded = backStackEntry.arguments?.getString("specData") ?: ""
                    val spec = Screen.SpecFullEdit.decodeSpec(encoded)
                    SpecFullEditScreen(
                        spec = spec,
                        onBack = { mainNavController.popBackStack() }
                    )
                }
                composable(Screen.Scrap.route) {
                    ScrapScreen(
                        onBack = { mainNavController.popBackStack() },
                        onNoticeClick = { noticeId ->
                            mainNavController.navigate(Screen.NoticeDetail.createRoute(noticeId))
                        }
                    )
                }
                composable(Screen.Notification.route) {
                    NotificationScreen(
                        onBack = { mainNavController.popBackStack() },
                        onNoticeClick = { noticeId ->
                            mainNavController.navigate(Screen.NoticeDetail.createRoute(noticeId))
                        }
                    )
                }
                composable(Screen.SupportNotice.route) {
                    SupportNoticeScreen(
                        onBack = { mainNavController.popBackStack() }
                    )
                }
            }
        }
    }
}