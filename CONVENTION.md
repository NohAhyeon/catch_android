# 📐 공지캐치 Android 개발 컨벤션

## 브랜치 전략
- `main` — 배포/제출 가능한 안정 버전만
- `develop` — 개발 통합 브랜치
- `feature/기능명` — 개별 기능 개발 (예: `feature/login`, `feature/notice-list`)
- `submit/mvp` — 과제 제출용 브랜치

## 커밋 메시지 컨벤션

```
[타입] 작업 내용 요약

예시:
[Feat] 공지사항 목록 화면 구현
[Fix] 로그인 토큰 만료 오류 수정
[Refactor] ViewModel 로직 분리
[Chore] 의존성 추가
[Docs] README 업데이트
```

타입 종류: `Feat`(기능 추가), `Fix`(버그 수정), `Refactor`(리팩토링), `Chore`(빌드/설정), `Docs`(문서), `Style`(포맷팅), `Test`(테스트)

## PR(Pull Request) 규칙
- 기능 단위로 작게 쪼개서 PR 올리기 (한 PR에 너무 많은 거 담지 않기)
- PR 제목도 커밋 컨벤션과 동일하게
- 상대방 리뷰 후 머지 (2인이라 서로 코드 한 번씩은 보고 넘어가기)
- 최소한 빌드 에러는 없는지 확인하고 PR 올리기

## 코드 컨벤션
- Kotlin 공식 스타일 가이드 기준
- 패키지 구조: `data`(local, remote, repository) / `domain`(model, repository, usecase) / `presentation`(component, home, notice, search, calendar, mypage, onboarding) / `util`
- 네이밍 규칙: 변수/함수는 camelCase, 리소스 파일은 snake_case

## Compose 컨벤션
- **State Hoisting 원칙**: Composable은 가능한 stateless하게 작성하고, 상태는 상위(ViewModel 또는 호출부)에서 관리하고 파라미터로 내려받기
- 화면 단위 Composable은 `~Screen`, 재사용 컴포넌트는 `~Item`, `~Card` 등 역할이 드러나는 네이밍 사용
- ViewModel의 상태는 `StateFlow`로 노출하고, Composable에서는 `collectAsState()`로 구독
- 재사용 가능한 컴포넌트에는 `@Preview` 작성 권장

## 디자인 시스템

### 색상
| 용도 | 값 |
|---|---|
| Primary | #403DE4 |
| Secondary Light | #E3E8FD |
| Secondary | #A2B2FD |
| Text Title | #111111 |
| Text Body | #4B5563 |
| Text Caption | #9CA3AF |
| Background | #F7F9FA |
| Divider | #A2B2FD |
| Inactive | #F0F1F3 |
| 마감임박(Deadline) | #FF5A5F |

### 타이포그래피 (Pretendard 폰트)
| 구분 | 모바일 크기 | 굵기 |
|---|---|---|
| 대제목(H1) | 24~28px | Bold |
| 중제목(H2) | 18~22px | Semibold |
| 본문(Body) | 15~17px | Regular |
| 보조문구(Caption) | 13~14px | Regular |
| 최소 크기 | 11px | - |

### 레이아웃 & 간격
- 기준 화면 너비 390px, 좌우 여백 항상 16px 또는 20px 고정
- 상단바 영역 44~47px는 시스템 영역이라 비워두기
- 내부 간격: 8px / 12px / 16px
- 요소 간 간격: 16px / 24px
- 섹션 간 간격: 40px / 56px / 80px (영역 구분 명확히)

### 버튼 규격
- 메인 버튼: 56px, filled primary
- 서브 버튼: 48px, outlined
- 하단 선택 버튼: 56px, filled (연한 primary)
- 작은 버튼(칩): 37px
- 터치 영역 기본: 44×44px

## 환경 설정 관련 주의사항

### `local.properties` 값 읽기
- `project.findProperty(...)`는 `local.properties`를 자동으로 읽지 않는다 (Gradle 함정!)
- `local.properties` 값을 `BuildConfig`에 반영하려면 `app/build.gradle.kts` 상단에서 `Properties()`로 직접 로드해야 함:
```kotlin
import java.util.Properties
import java.io.FileInputStream

val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localProperties.load(FileInputStream(localPropertiesFile))
}
```
- 이후 `localProperties.getProperty("KEY_NAME")` 형태로 사용

### AndroidManifest.xml 수정 시 주의
- 새 `<activity>`나 SDK 관련 태그 추가할 때, 기존에 있던 **`MainActivity`(LAUNCHER 진입점)**와 **`<uses-permission android:name="android.permission.INTERNET" />`**를 실수로 삭제하지 않도록 주의
- 매니페스트 전체 삭제 후 재작성하지 말고, 필요한 부분만 추가/수정할 것

### API 연동 시 참고
- Mock Repository → 실제 Repository 전환 시, `RepositoryModule.kt`의 `@Binds` 바인딩 대상만 교체하면 됨 (ViewModel/Screen 코드는 그대로 유지)
- 인증이 필요한 API는 OkHttp `Interceptor`로 `Authorization` 헤더 자동 첨부 처리
