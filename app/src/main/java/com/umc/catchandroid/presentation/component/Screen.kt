package com.umc.catchandroid.presentation.component

import com.umc.catchandroid.domain.model.Spec
import java.net.URLDecoder
import java.net.URLEncoder

sealed class Screen(val route: String) {
    // 인증/온보딩
    object Login : Screen("login")
    object Splash : Screen("splash?provider={provider}") {
        fun createRoute(provider: String = "kakao") = "splash?provider=$provider"
    }
    object OnboardingUniversity : Screen("onboarding_university/{provider}") {
        fun createRoute(provider: String) = "onboarding_university/$provider"
    }
    object OnboardingProfile : Screen("onboarding_profile/{universityId}") {
        fun createRoute(universityId: Long) = "onboarding_profile/$universityId"
    }

    object DepartmentSearch : Screen("department_search/{universityId}") {
        fun createRoute(universityId: Long) = "department_search/$universityId"
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

    // 마이페이지 하위
    object KeywordManage : Screen("keyword_manage")
    object SchoolInfoEdit : Screen("school_info_edit")
    object NotificationSettings : Screen("notification_settings")
    object SpecLog : Screen("spec_log")
    object SpecAdd : Screen("spec_add")
    object SpecDetail : Screen("spec_detail/{specData}") {
        private const val DELIMITER = "|||"

        fun createRoute(spec: Spec): String {
            val raw = listOf(
                spec.specId.toString(),
                spec.category,
                spec.title,
                spec.organization,
                spec.specDate,
                spec.scoreOrGrade ?: "",
                spec.memo ?: ""
            ).joinToString(DELIMITER)
            return "spec_detail/${URLEncoder.encode(raw, "UTF-8")}"
        }

        fun decodeSpec(encoded: String): Spec {
            val raw = URLDecoder.decode(encoded, "UTF-8")
            val parts = raw.split(DELIMITER)
            return Spec(
                specId = parts[0].toLong(),
                category = parts[1],
                categoryTag = "",
                title = parts[2],
                organization = parts[3],
                specDate = parts[4],
                scoreOrGrade = parts[5].ifEmpty { null },
                memo = parts[6].ifEmpty { null }
            )
        }
    }
    // 스펙 상세 화면 상단 요약 카드를 눌렀을 때 이동하는 전체 필드 수정 화면
    object SpecFullEdit : Screen("spec_full_edit/{specData}") {
        private const val DELIMITER = "|||"

        fun createRoute(spec: Spec): String {
            val raw = listOf(
                spec.specId.toString(),
                spec.category,
                spec.title,
                spec.organization,
                spec.specDate,
                spec.scoreOrGrade ?: "",
                spec.memo ?: ""
            ).joinToString(DELIMITER)
            return "spec_full_edit/${URLEncoder.encode(raw, "UTF-8")}"
        }

        fun decodeSpec(encoded: String): Spec {
            val raw = URLDecoder.decode(encoded, "UTF-8")
            val parts = raw.split(DELIMITER)
            return Spec(
                specId = parts[0].toLong(),
                category = parts[1],
                categoryTag = "",
                title = parts[2],
                organization = parts[3],
                specDate = parts[4],
                scoreOrGrade = parts[5].ifEmpty { null },
                memo = parts[6].ifEmpty { null }
            )
        }
    }
    object Scrap : Screen("scrap")
    object Notification : Screen("notification")
    object SupportNotice : Screen("support_notice")
}