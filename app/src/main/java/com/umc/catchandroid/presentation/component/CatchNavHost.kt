package com.umc.catchandroid.presentation.component

import android.util.Log
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
import retrofit2.HttpException
import javax.inject.Inject

// 스플래시에서 판단할 상태
sealed class SplashDestination {

    // 현재 로그인/프로필 상태를 확인하는 중
    object Loading : SplashDestination()

    // 토큰이 존재하고 온보딩까지 완료된 사용자
    object GoHome : SplashDestination()

    // 토큰은 있지만 학교/학과/학년 정보가 없는 사용자
    data class GoOnboarding(
        val provider: String
    ) : SplashDestination()

    // 토큰이 없거나 인증이 만료된 사용자
    object GoLogin : SplashDestination()
}


@HiltViewModel
class SplashViewModel @Inject constructor(
    private val tokenManager: TokenManager,
    private val userRepository: UserRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    // 로그인 직후에는 로그인 provider가 전달됨.
    // 앱을 그냥 실행한 경우에는 기본값 kakao 사용.
    private val provider: String =
        savedStateHandle.get<String>("provider") ?: "kakao"

    var destination by mutableStateOf<SplashDestination>(
        SplashDestination.Loading
    )
        private set


    init {
        checkLoginState()
    }


    private fun checkLoginState() {

        viewModelScope.launch {

            // 1. 저장된 AccessToken 확인
            val token = tokenManager.getAccessToken()

            if (token.isNullOrBlank()) {

                Log.d(
                    TAG,
                    "저장된 AccessToken 없음 -> 로그인 화면 이동"
                )

                destination = SplashDestination.GoLogin
                return@launch
            }


            // 2. 토큰이 존재하면 사용자 프로필 조회
            try {

                val profile = userRepository.getUserProfile()

                Log.d(
                    TAG,
                    "프로필 조회 성공: $profile"
                )


                /*
                 * 서버에서는 온보딩을 완료하지 않은 사용자의 경우
                 *
                 * universityName = null
                 * departmentName = null
                 * grade = null
                 *
                 * 형태로 내려올 수 있음.
                 *
                 * 따라서 null 여부까지 고려해서 온보딩 완료 여부 판단.
                 */
                val onboardingDone =
                    !profile.universityName.isNullOrBlank() &&
                            !profile.departmentName.isNullOrBlank() &&
                            (profile.grade ?: 0) > 0


                Log.d(
                    TAG,
                    "온보딩 완료 여부 = $onboardingDone"
                )


                destination =
                    if (onboardingDone) {

                        Log.d(
                            TAG,
                            "온보딩 완료 -> Home 이동"
                        )

                        SplashDestination.GoHome

                    } else {

                        Log.d(
                            TAG,
                            "온보딩 미완료 -> Onboarding 이동"
                        )

                        SplashDestination.GoOnboarding(
                            provider = provider
                        )
                    }


            } catch (e: HttpException) {

                /*
                 * HTTP 오류
                 *
                 * 401이면 AccessToken 자체가 만료되었거나 잘못된 것이므로
                 * 토큰을 삭제하고 로그인 화면으로 이동.
                 *
                 * 다른 HTTP 오류라고 해서 정상 토큰까지 삭제하면 안 됨.
                 */
                Log.e(
                    TAG,
                    "프로필 조회 HTTP 오류: ${e.code()}",
                    e
                )


                if (e.code() == 401) {

                    Log.d(
                        TAG,
                        "401 Unauthorized -> 저장된 토큰 삭제"
                    )

                    tokenManager.clearTokens()
                }


                destination = SplashDestination.GoLogin


            } catch (e: Exception) {

                /*
                 * JSON 파싱 오류, NullPointerException,
                 * 네트워크 오류 등 일반적인 오류.
                 *
                 * 여기서는 토큰을 무조건 삭제하지 않는다.
                 */
                Log.e(
                    TAG,
                    "프로필 조회 중 예외 발생",
                    e
                )

                destination = SplashDestination.GoLogin
            }
        }
    }


    companion object {
        private const val TAG = "SplashViewModel"
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

        /*
         * Splash
         *
         * 앱 시작 또는 로그인 성공 후 여기로 들어와서
         *
         * 1. Token 확인
         * 2. Profile 조회
         * 3. 온보딩 완료 여부 확인
         *
         * 을 수행한다.
         */
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

                    is SplashDestination.GoHome -> {

                        navController.navigate(
                            Screen.Home.route
                        ) {

                            popUpTo(
                                Screen.Splash.route
                            ) {
                                inclusive = true
                            }
                        }
                    }


                    is SplashDestination.GoOnboarding -> {

                        navController.navigate(
                            Screen.OnboardingUniversity.createRoute(
                                dest.provider
                            )
                        ) {

                            popUpTo(
                                Screen.Splash.route
                            ) {
                                inclusive = true
                            }
                        }
                    }


                    is SplashDestination.GoLogin -> {

                        navController.navigate(
                            Screen.Login.route
                        ) {

                            popUpTo(
                                Screen.Splash.route
                            ) {
                                inclusive = true
                            }
                        }
                    }


                    is SplashDestination.Loading -> {
                        // 아직 로그인 상태 확인 중
                    }
                }
            }


            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {

                CircularProgressIndicator(
                    color = CatchPrimary
                )
            }
        }


        /*
         * 로그인
         */
        composable(
            Screen.Login.route
        ) {

            LoginScreen(

                onLoginSuccess = { provider ->

                    /*
                     * 로그인 성공했다고 바로 온보딩으로 보내지 않는다.
                     *
                     * Splash에서 실제 프로필을 조회한 후
                     *
                     * 온보딩 완료 -> Home
                     * 온보딩 미완료 -> Onboarding
                     *
                     * 으로 분기한다.
                     */
                    navController.navigate(
                        Screen.Splash.createRoute(
                            provider
                        )
                    ) {

                        popUpTo(
                            Screen.Login.route
                        ) {
                            inclusive = true
                        }
                    }
                }
            )
        }


        /*
         * 온보딩 - 대학교 선택
         */
        composable(
            Screen.OnboardingUniversity.route
        ) { backStackEntry ->

            val provider =
                backStackEntry.arguments
                    ?.getString("provider")
                    ?: "kakao"


            OnboardingUniversityScreen(

                provider = provider,

                onNext = { universityId ->

                    navController.navigate(
                        Screen.OnboardingProfile.createRoute(
                            universityId
                        )
                    )
                }
            )
        }


        /*
         * 온보딩 - 학과 / 학년
         */
        composable(
            Screen.OnboardingProfile.route
        ) { backStackEntry ->

            val universityId =
                backStackEntry.arguments
                    ?.getString("universityId")
                    ?.toLongOrNull()
                    ?: 1L


            /*
             * 학과 검색 화면에서 선택한 값을
             * 다시 받아오기 위한 상태
             */
            var selectedDepartment by remember {
                mutableStateOf<Department?>(null)
            }


            val savedStateHandle =
                navController
                    .currentBackStackEntry
                    ?.savedStateHandle


            LaunchedEffect(
                savedStateHandle
            ) {

                val idFlow =
                    savedStateHandle
                        ?.getStateFlow<Long?>(
                            "selected_department_id",
                            null
                        )


                val nameFlow =
                    savedStateHandle
                        ?.getStateFlow<String?>(
                            "selected_department_name",
                            null
                        )


                if (
                    idFlow != null &&
                    nameFlow != null
                ) {

                    combine(
                        idFlow,
                        nameFlow
                    ) { id, name ->

                        if (
                            id != null &&
                            name != null
                        ) {

                            Department(
                                id,
                                name
                            )

                        } else {

                            null
                        }

                    }.collect { department ->

                        if (department != null) {

                            selectedDepartment =
                                department
                        }
                    }
                }
            }


            OnboardingProfileScreen(

                universityId =
                    universityId,

                selectedDepartment =
                    selectedDepartment,

                onDepartmentSearchClick = {

                    navController.navigate(
                        Screen.DepartmentSearch.createRoute(
                            universityId
                        )
                    )
                },

                onNext = {

                    navController.navigate(
                        Screen.OnboardingKeyword.route
                    )
                }
            )
        }


        /*
         * 학과 검색
         */
        composable(
            Screen.DepartmentSearch.route
        ) { backStackEntry ->

            val universityId =
                backStackEntry.arguments
                    ?.getString("universityId")
                    ?.toLongOrNull()
                    ?: 1L


            DepartmentSearchScreen(

                universityId =
                    universityId,

                onBack = {

                    navController.popBackStack()
                },

                onDepartmentSelected = { department ->

                    navController
                        .previousBackStackEntry
                        ?.savedStateHandle
                        ?.apply {

                            set(
                                "selected_department_id",
                                department.departmentId
                            )

                            set(
                                "selected_department_name",
                                department.departmentName
                            )
                        }


                    navController.popBackStack()
                }
            )
        }


        /*
         * 온보딩 - 키워드
         */
        composable(
            Screen.OnboardingKeyword.route
        ) {

            OnboardingKeywordScreen(

                onComplete = {

                    navController.navigate(
                        Screen.Home.route
                    ) {

                        /*
                         * 온보딩 완료 후 이전 온보딩 화면들을
                         * 모두 제거한다.
                         */
                        popUpTo(0)
                    }
                }
            )
        }


        /*
         * 홈
         */
        composable(
            Screen.Home.route
        ) {

            MainScreen(

                onLogout = {

                    navController.navigate(
                        Screen.Login.route
                    ) {

                        popUpTo(0)
                    }
                }
            )
        }
    }
}