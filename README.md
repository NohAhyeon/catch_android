# NoticeCatchFE
대학 공지사항 알림 서비스 - 프론트엔드

# 📱 공지캐치 (Notice-Catch) - Android

대학 공지사항을 효율적으로 모아 유저 맞춤형 피드와 스마트한 알림을 제공하는 안드로이드 앱의 프론트엔드 저장소입니다.

---

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| Language | Kotlin |
| UI | Jetpack Compose |
| Architecture | MVVM + Clean Architecture |
| DI | Hilt |
| Network | Retrofit2 + OkHttp3 |
| 비동기 | Kotlin Coroutines + Flow |
| 로컬 저장소 | DataStore |
| 이미지 로딩 | Coil |
| 푸시 알림 | FCM (Firebase Cloud Messaging) |
| 빌드 | Gradle Version Catalog |

---

## ✨ Key Features (서비스 기능 정의)

### 🔐 1. 회원가입 및 맞춤형 온보딩
- 소셜 로그인: 카카오, 구글 계정을 이용해 간편하게 회원가입 및 로그인
- 소속 대학/학과 설정: 대학교와 학과를 검색해 프로필에 등록
- 개인 프로필 완성: 학년 정보와 함께 첫 온보딩 시 관심 키워드 설정

### 📅 2. 홈 공지사항 피드 & 상세 조회
- 통합 공지 피드: 카테고리 필터 칩으로 학교 공지를 쾌적하게 조회
- 공지 상세 보기: 본문 내용, AI 3줄 요약, 원문 링크, 마감 D-Day 확인
- 키워드 검색: 최근 검색어 관리 및 정렬 기준에 맞춰 공지 검색

### 🔑 3. 맞춤형 관심 키워드 관리
- 추천 키워드 기반 등록: 인기 키워드(장학금, 비교과, 취업 등) 선택
- 나만의 커스텀 키워드: 직접 입력해 자유롭게 추가·수정·삭제

### 📆 4. 캘린더 & 마감 일정 관리
- 월별 캘린더에서 마감 임박 공지를 한눈에 확인
- 선택한 날짜의 공지 목록 및 다가오는 일정 리스트 제공

### ⚙️ 5. 마이페이지 및 맞춤형 알림 설정
- 내 프로필 조회: 등록한 대학교, 학과, 학년 정보 확인
- 스크랩 / 스펙 로그 / 관심 키워드 관리
- 세부 알림 토글 제어: 카테고리별 알림 On/Off

---

## 🚀 Getting Started (로컬 개발 환경 실행 방법)

### 📋 1. 사전 요구사항 (Prerequisites)
- Android Studio (최신 안정 버전 권장)
- JDK 11 이상
- Android SDK (minSdk 26 / targetSdk 36)

### 🛠️ 2. 소스코드 복사 및 실행 (Quick Start)

**Step 1. 저장소 복사하기**

    git clone https://github.com/notice-catch/NoticeCatchFE.git
    cd NoticeCatchFE

**Step 2. Android Studio에서 프로젝트 열기**

**Step 3. `local.properties`에 서버 URL 등록**

    BASE_URL=서버 URL

**Step 4. Run 버튼을 눌러 빌드** (minSdk 26 이상)

---

## 📂 프로젝트 폴더 구조

    app/
    ├── data/
    │   ├── local/          # DataStore, Room 등 로컬 데이터
    │   ├── remote/         # Retrofit API 인터페이스, DTO
    │   └── repository/     # Repository 구현체
    ├── domain/
    │   ├── model/          # 도메인 모델
    │   ├── repository/     # Repository 인터페이스
    │   └── usecase/        # UseCase
    ├── presentation/
    │   ├── component/      # 공통 Composable 컴포넌트 (Navigation 등)
    │   ├── home/            # 홈 화면
    │   ├── notice/          # 공지 상세 화면
    │   ├── search/          # 검색 화면
    │   ├── calendar/        # 캘린더 화면
    │   ├── mypage/           # 마이페이지
    │   └── onboarding/      # 온보딩 / 로그인 화면
    └── util/                # 공통 유틸

---

## 📱 화면 목록 & 플로우

| 화면 이름 | 스크린 ID | 진입 경로 | 진행 상태 |
|-----------|-----------|----------|------|
| 로그인 | LoginScreen | 앱 최초 실행 | ✅ 완료 (카카오 SDK 실연동) |
| 온보딩 - 대학 선택 | OnboardingUniversityScreen | 로그인 완료 후 최초 1회 | ✅ 완료 |
| 온보딩 - 프로필 설정 | OnboardingProfileScreen | 대학 선택 후 | ✅ 완료 |
| 온보딩 - 관심 키워드 | OnboardingKeywordScreen | 프로필 설정 후 | ✅ 완료 |
| 홈 | HomeScreen | 설정 완료 후 / 메인 진입점 | ✅ 완료 (실 API 연동) |
| 공지 상세 | NoticeDetailScreen | 홈 → 아이템 클릭 | ✅ 완료 (Mock 연동) |
| 검색 | SearchScreen | 하단 탭 → 검색 | ✅ 완료 (Mock 연동) |
| 캘린더 | CalendarScreen | 하단 탭 → 캘린더 | ✅ 완료 (Mock 연동) |
| 마이페이지 | MyPageScreen | 하단 탭 → 마이페이지 | ✅ 완료 (Mock 연동) |

### 네비게이션 플로우

    앱 실행
     └─ 최초 설치
         └─ 로그인 → 온보딩(대학선택→프로필설정→키워드설정) → 홈
     └─ 재실행 (로그인 유지)
         └─ 홈 (하단 탭: 홈 / 검색 / 캘린더 / 마이)
              ├─ 홈 → 공지 상세
              ├─ 검색
              ├─ 캘린더
              └─ 마이페이지

---

## 🔗 연결 정보
- Figma 디자인 시스템: https://www.figma.com/design/wt4Av7o4wtnAYe6FIJIyPl/Untitled?node-id=0-1
- 백엔드 API 명세서(Swagger): https://noticecatch.duckdns.org/swagger-ui/index.html

---

## 🤝 프로젝트의 일관성을 위한 공지캐치 Android 개발 컨벤션 규칙 보러가기
→ [CONVENTION.md](./CONVENTION.md) 참고
