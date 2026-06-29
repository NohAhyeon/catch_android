# 📢 공지캐치

> 흩어진 교내 공지, 이제 한 곳에서 — 전공·관심 키워드 기반 맞춤 공지 큐레이션 앱

---

## 프로젝트 소개

대학생들은 학사·장학·비교과 정보를 얻기 위해 매일 학교 홈페이지, 학생정보시스템, 단과대 사이트 등 여러 채널을 일일이 순회해야 합니다. **공지캐치**는 흩어진 교내 공지를 자동 수집하고, AI가 핵심 내용을 3줄로 요약해 사용자의 전공·관심 키워드에 맞게 푸시 알림으로 전달합니다.

---

## 팀원 소개

| 역할 | 닉네임 | 이름 |
|------|--------|------|
| PM | 갱갱 | 김준경 |
| PM | 호호 | 김철호 |
| Design | 은 | 전소은 |
| Android | 놔현 | 노아현 |
| Android | 뗀석기유저 | 황지원 |
| Spring Boot | 레몬 | 박세은 |
| Spring Boot | 베리 | 김창령 |

---

## 기술 스택

| 분류 | 내용 |
|------|------|
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

## 프로젝트 폴더 구조

```
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
│   ├── component/      # 공통 Composable 컴포넌트
│   ├── home/           # 홈 화면
│   ├── notice/         # 공지 목록 / 상세 화면
│   ├── alarm/          # 알림 화면
│   ├── mypage/         # 마이페이지
│   └── onboarding/     # 온보딩 / 로그인 화면
└── util/               # 공통 유틸
```

---

## 컨벤션 문서

→ [CONVENTION.md](./CONVENTION.md) 참고

---

## 빌드 및 실행 방법

1. 본 레포지토리를 클론합니다.
   ```bash
   git clone https://github.com/NohAhyeon/catch_android.git
   ```
2. Android Studio에서 프로젝트를 엽니다.
3. `local.properties`에 아래 키를 추가합니다.
   ```
   BASE_URL=서버 URL
   ```
4. Run 버튼을 눌러 빌드합니다. (minSdk 26 이상)

---

## 화면 목록 & 플로우

| 화면 이름 | 스크린 ID | 진입 경로 | 담당자 |
|-----------|-----------|-----------|--------|
| 스플래시 | SplashScreen | 앱 최초 실행 | |
| 온보딩 | OnboardingScreen | 스플래시 → 최초 설치 시 | |
| 로그인 | LoginScreen | 온보딩 완료 후 / 미로그인 상태 | |
| 학적 등록 | ProfileSetupScreen | 로그인 완료 후 최초 1회 | |
| 키워드 설정 | KeywordSetupScreen | 학적 등록 완료 후 최초 1회 | |
| 홈 | HomeScreen | 설정 완료 후 / 메인 진입점 | |
| 공지 목록 | NoticeListScreen | 홈 → 카테고리 탭 선택 | |
| 공지 상세 | NoticeDetailScreen | 공지 목록 → 아이템 클릭 | |
| 알림 | AlarmScreen | 하단 탭 → 알림 | |
| 마이페이지 | MyPageScreen | 하단 탭 → 마이페이지 | |
| 키워드 관리 | KeywordManageScreen | 마이페이지 → 키워드 설정 | |

### 네비게이션 플로우

```
앱 실행
 └─ 최초 설치
     ├─ 온보딩 → 로그인 → 학적 등록 → 키워드 설정 → 홈
     └─ 재실행 (로그인 유지)
         └─ 홈
              ├─ 공지 목록 → 공지 상세
              ├─ 알림
              └─ 마이페이지 → 키워드 관리
```
