package com.umc.catchandroid.presentation.component

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.umc.catchandroid.presentation.onboarding.LoginScreen
import com.umc.catchandroid.presentation.onboarding.OnboardingKeywordScreen
import com.umc.catchandroid.presentation.onboarding.OnboardingProfileScreen
import com.umc.catchandroid.presentation.onboarding.OnboardingUniversityScreen

@Composable
fun CatchNavHost(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        // 로그인
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = { provider ->
                    navController.navigate(Screen.OnboardingUniversity.createRoute(provider)) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        // 온보딩
        composable(Screen.OnboardingUniversity.route) { backStackEntry ->
            val provider = backStackEntry.arguments?.getString("provider") ?: "kakao"
            OnboardingUniversityScreen(
                provider = provider,
                onNext = { navController.navigate(Screen.OnboardingProfile.route) }
            )
        }
        composable(Screen.OnboardingProfile.route) {
            OnboardingProfileScreen(
                onNext = { navController.navigate(Screen.OnboardingKeyword.route) }
            )
        }
        composable(Screen.OnboardingKeyword.route) {
            OnboardingKeywordScreen(
                onComplete = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Home.route) {
            MainScreen()
        }
    }
}