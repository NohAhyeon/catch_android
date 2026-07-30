package com.umc.catchandroid.presentation.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
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
import com.umc.catchandroid.presentation.mypage.SpecAddScreen
import com.umc.catchandroid.presentation.mypage.SpecDetailScreen
import com.umc.catchandroid.presentation.mypage.SpecLogScreen
import com.umc.catchandroid.presentation.notice.NoticeDetailScreen
import com.umc.catchandroid.presentation.search.SearchScreen
import com.umc.catchandroid.presentation.notice.WebViewScreen

@Composable
fun MainScreen(
    onLogout: () -> Unit = {}
) {
    val mainNavController = rememberNavController()

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
                    }
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
                    }
                )
            }
            composable(Screen.Calendar.route) {
                CalendarScreen(
                    onNoticeClick = { noticeId ->
                        mainNavController.navigate(Screen.NoticeDetail.createRoute(noticeId))
                    }
                )
            }
            composable(Screen.MyPage.route) {
                MyPageScreen(
                    onLogout = onLogout,
                    onKeywordManageClick = { mainNavController.navigate(Screen.KeywordManage.route) },
                    onSchoolInfoClick = { mainNavController.navigate(Screen.SchoolInfoEdit.route) },
                    onNotificationSettingsClick = { mainNavController.navigate(Screen.NotificationSettings.route) },
                    onSpecLogClick = { mainNavController.navigate(Screen.SpecLog.route) }
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
                    onBack = { mainNavController.popBackStack() }
                )
            }
        }
    }
}