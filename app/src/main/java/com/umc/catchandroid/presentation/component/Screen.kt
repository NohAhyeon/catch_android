package com.umc.catchandroid.presentation.component

import java.net.URLDecoder
import java.net.URLEncoder

sealed class Screen(val route: String) {
    // 인증/온보딩
    object Login : Screen("login")
    object OnboardingUniversity : Screen("onboarding_university/{provider}") {
        fun createRoute(provider: String) = "onboarding_university/$provider"
    }
    object OnboardingProfile : Screen("onboarding_profile/{universityId}") {
        fun createRoute(universityId: Long) = "onboarding_profile/$universityId"
    }
    object OnboardingKeyword : Screen("onboarding_keyword")

    // 메인 (하단 탭)
    object Home : Screen("home")
    object Search : Screen("search")
    object Calendar : Screen("calendar")
    object MyPage : Screen("mypage")

    // 홈 하위
    object NoticeDetail : Screen("notice_detail/{noticeId}") {
        fun createRoute(noticeId: Long) = "notice_detail/$noticeId"
    }
    object WebView : Screen("web_view/{url}") {
        fun createRoute(url: String) = "web_view/${URLEncoder.encode(url, "UTF-8")}"
        fun decodeUrl(encoded: String) = URLDecoder.decode(encoded, "UTF-8")
    }
}