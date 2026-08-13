package com.umc.catchandroid.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.umc.catchandroid.R
import com.umc.catchandroid.ui.theme.CatchPrimary
import com.umc.catchandroid.ui.theme.CatchTextCaption
import androidx.compose.ui.graphics.Color

data class BottomNavItem(
    val screen: Screen,
    val label: String,
    val iconRes: Int
)

val bottomNavItems = listOf(
    BottomNavItem(Screen.Home, "홈", R.drawable.ic_home),
    BottomNavItem(Screen.Search, "검색", R.drawable.ic_search),
    BottomNavItem(Screen.Calendar, "캘린더", R.drawable.ic_calendar),
    BottomNavItem(Screen.MyPage, "마이", R.drawable.ic_mypage)
)

// 아이콘 크기: 바깥 Box와 안쪽 Image 크기를 반드시 동일하게 유지해야 함
// (부모 Box의 크기 제약이 항상 우선 적용되므로, 안쪽 Image만 키워도 소용없음)
private val NAV_ICON_SIZE = 38.dp

@Composable
fun BottomNavBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(containerColor = androidx.compose.ui.graphics.Color.White) {
        bottomNavItems.forEach { item ->
            val selected = currentRoute == item.screen.route
            val tintColor = if (selected) CatchPrimary else Color(0xFFBFBFBF)

            NavigationBarItem(
                selected = selected,
                onClick = {
                    navController.navigate(item.screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Box(
                        modifier = Modifier.size(NAV_ICON_SIZE),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = item.iconRes),
                            contentDescription = item.label,
                            colorFilter = ColorFilter.tint(tintColor),
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.size(NAV_ICON_SIZE)
                        )
                    }
                },
                label = null,
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = androidx.compose.ui.graphics.Color.Transparent
                )
            )
        }
    }
}