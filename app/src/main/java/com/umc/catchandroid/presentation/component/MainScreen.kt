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
import com.umc.catchandroid.presentation.mypage.MyPageScreen
import com.umc.catchandroid.presentation.notice.NoticeDetailScreen
import com.umc.catchandroid.presentation.search.SearchScreen

@Composable
fun MainScreen() {
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
                    onBack = { mainNavController.popBackStack() }
                )
            }
            composable(Screen.Search.route) { SearchScreen() }
            composable(Screen.Calendar.route) { CalendarScreen() }
            composable(Screen.MyPage.route) { MyPageScreen() }
        }
    }
}