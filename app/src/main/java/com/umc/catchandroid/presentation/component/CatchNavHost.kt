package com.umc.catchandroid.presentation.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.umc.catchandroid.domain.model.Department
import com.umc.catchandroid.presentation.onboarding.DepartmentSearchScreen
import com.umc.catchandroid.presentation.onboarding.LoginScreen
import com.umc.catchandroid.presentation.onboarding.OnboardingKeywordScreen
import com.umc.catchandroid.presentation.onboarding.OnboardingProfileScreen
import com.umc.catchandroid.presentation.onboarding.OnboardingUniversityScreen
import kotlinx.coroutines.flow.combine

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
                onNext = { universityId ->
                    navController.navigate(Screen.OnboardingProfile.createRoute(universityId))
                }
            )
        }
        composable(Screen.OnboardingProfile.route) { backStackEntry ->
            val universityId = backStackEntry.arguments?.getString("universityId")?.toLongOrNull() ?: 1L

            // 학과 검색 화면에서 선택 결과를 돌려받기 위한 state
            var selectedDepartment by remember { mutableStateOf<Department?>(null) }
            val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle

            LaunchedEffect(savedStateHandle) {
                val idFlow = savedStateHandle?.getStateFlow<Long?>("selected_department_id", null)
                val nameFlow = savedStateHandle?.getStateFlow<String?>("selected_department_name", null)
                if (idFlow != null && nameFlow != null) {
                    combine(idFlow, nameFlow) { id, name ->
                        if (id != null && name != null) Department(id, name) else null
                    }.collect { dept ->
                        if (dept != null) {
                            selectedDepartment = dept
                        }
                    }
                }
            }

            OnboardingProfileScreen(
                universityId = universityId,
                selectedDepartment = selectedDepartment,
                onDepartmentSearchClick = {
                    navController.navigate(Screen.DepartmentSearch.createRoute(universityId))
                },
                onNext = { navController.navigate(Screen.OnboardingKeyword.route) }
            )
        }
        composable(Screen.DepartmentSearch.route) { backStackEntry ->
            val universityId = backStackEntry.arguments?.getString("universityId")?.toLongOrNull() ?: 1L
            DepartmentSearchScreen(
                universityId = universityId,
                onBack = { navController.popBackStack() },
                onDepartmentSelected = { dept ->
                    navController.previousBackStackEntry?.savedStateHandle?.apply {
                        set("selected_department_id", dept.departmentId)
                        set("selected_department_name", dept.departmentName)
                    }
                    navController.popBackStack()
                }
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
            MainScreen(
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0)
                    }
                }
            )
        }
    }
}