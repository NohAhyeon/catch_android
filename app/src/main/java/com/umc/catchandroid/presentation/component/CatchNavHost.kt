package com.umc.catchandroid.presentation.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.umc.catchandroid.data.local.TokenManager
import com.umc.catchandroid.domain.model.Department
import com.umc.catchandroid.domain.repository.UserRepository
import com.umc.catchandroid.presentation.onboarding.DepartmentSearchScreen
import com.umc.catchandroid.presentation.onboarding.LoginScreen
import com.umc.catchandroid.presentation.onboarding.OnboardingKeywordScreen
import com.umc.catchandroid.presentation.onboarding.OnboardingProfileScreen
import com.umc.catchandroid.presentation.onboarding.OnboardingUniversityScreen
import com.umc.catchandroid.ui.theme.CatchPrimary
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

// 스플래시에서 판단할 상태
// (SplashViewModel의 public 프로퍼티가 이 타입을 노출하므로 private으로 두면 안 됨)
sealed class SplashDestination {
    object Loading : SplashDestination()
    object GoHome : SplashDestination()                    // 토큰 있고 온보딩(학과/학년)까지 완료
    data class GoOnboarding(val provider: String) : SplashDestination() // 토큰은 있지만 온보딩 미완료
    object GoLogin : SplashDestination()                    // 토큰 없음 또는 만료/오류
}

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val tokenManager: TokenManager,
    private val userRepository: UserRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    // 로그인 성공 직후 진입 시에는 provider가 넘어오고, 앱 콜드스타트 시에는 기본값 사용
    private val provider: String = savedStateHandle.get<String>("provider") ?: "kakao"

    var destination by mutableStateOf<SplashDestination>(SplashDestination.Loading)
        private set

    init {
        viewModelScope.launch {
            val token = tokenManager.getAccessToken()
            if (token.isNullOrBlank()) {
                destination = SplashDestination.GoLogin
                return@launch
            }

            destination = try {
                val profile = userRepository.getUserProfile()
                // 학과/학년이 비어있으면 온보딩 미완료로 판단
                val onboardingDone = profile.departmentName.isNotBlank() && profile.grade > 0
                if (onboardingDone) SplashDestination.GoHome else SplashDestination.GoOnboarding(provider)
            } catch (e: Exception) {
                // 토큰 만료/무효(401) 등 - 저장된 토큰 지우고 재로그인 유도
                tokenManager.clearTokens()
                SplashDestination.GoLogin
            }
        }
    }
}

@Composable
fun CatchNavHost(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        // 스플래시: 저장된 토큰 + 온보딩 완료 여부 확인 후 자동 분기
        // 앱 콜드스타트(provider 없음)와 로그인 직후(provider 있음) 둘 다 여기를 거침
        composable(
            route = Screen.Splash.route,
            arguments = listOf(
                navArgument("provider") {
                    type = NavType.StringType
                    defaultValue = "kakao"
                }
            )
        ) {
            val viewModel: SplashViewModel = hiltViewModel()
            val destination = viewModel.destination

            LaunchedEffect(destination) {
                when (val dest = destination) {
                    is SplashDestination.GoHome -> navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                    is SplashDestination.GoOnboarding -> navController.navigate(
                        Screen.OnboardingUniversity.createRoute(dest.provider)
                    ) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                    is SplashDestination.GoLogin -> navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                    is SplashDestination.Loading -> Unit // 확인중, 로딩만 표시
                }
            }

            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = CatchPrimary)
            }
        }

        // 로그인
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = { provider ->
                    // 온보딩으로 바로 보내지 않고 Splash를 경유 -> 이미 온보딩 끝낸 유저면 Home으로
                    navController.navigate(Screen.Splash.createRoute(provider)) {
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