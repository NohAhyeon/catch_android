# 공지캐치 Android 컨벤션

---

## 브랜치 네이밍 규칙

```
<type>/<간단한 설명>
```

| 타입 | 설명 |
|------|------|
| `feature` | 새 기능 개발 |
| `fix` | 버그 수정 |
| `refactor` | 리팩토링 (기능 변경 없음) |
| `chore` | 빌드, 설정, 의존성 변경 |
| `docs` | 문서 작성/수정 |

**예시**
```
feature/login-screen
feature/notice-detail
fix/crash-on-alarm
refactor/home-viewmodel
chore/add-retrofit-dependency
```

---

## 커밋 메시지 규칙

```
<type>: <내용> (#이슈번호)
```

| 타입 | 설명 |
|------|------|
| `feat` | 새 기능 추가 |
| `fix` | 버그 수정 |
| `refactor` | 리팩토링 |
| `chore` | 빌드/설정 변경 |
| `docs` | 문서 수정 |
| `style` | 코드 포맷, 세미콜론 누락 등 (로직 변경 없음) |
| `test` | 테스트 추가/수정 |

**예시**
```
feat: 공지 상세 화면 AI 요약 카드 구현 (#12)
fix: 홈 화면 리스트 스크롤 위치 초기화 버그 수정 (#15)
chore: Hilt 의존성 추가 (#8)
```

---

## PR 규칙

- **브랜치**: `feature/*` → `develop` 으로 PR
- **제목 형식**: `[feat] 공지 상세 화면 구현`
- **리뷰어**: Android 팀원 전원 필수 지정
- **머지 조건**: 리뷰어 1명 이상 Approve 후 본인이 머지
- **머지 방식**: Squash and Merge
- **PR 크기**: 하나의 PR은 하나의 기능 단위로 유지 (리뷰 용이성)

**PR 템플릿 항목**
- 작업 내용 요약
- 스크린샷 (UI 변경 시 필수)
- 관련 이슈 번호

---

## 코드 네이밍 규칙

### 파일 / 클래스

| 대상 | 규칙 | 예시 |
|------|------|------|
| Composable 함수 | PascalCase | `NoticeDetailScreen` |
| ViewModel | PascalCase + ViewModel 접미사 | `NoticeDetailViewModel` |
| UseCase | PascalCase + UseCase 접미사 | `GetNoticeDetailUseCase` |
| Repository 인터페이스 | PascalCase + Repository 접미사 | `NoticeRepository` |
| Repository 구현체 | PascalCase + RepositoryImpl 접미사 | `NoticeRepositoryImpl` |
| DTO | PascalCase + Dto 접미사 | `NoticeResponseDto` |
| 도메인 모델 | PascalCase | `Notice` |

### 변수 / 함수

| 대상 | 규칙 | 예시 |
|------|------|------|
| 변수 | camelCase | `noticeList`, `isLoading` |
| 함수 | camelCase | `fetchNoticeDetail()` |
| 상수 | UPPER_SNAKE_CASE | `MAX_RETRY_COUNT` |
| 리소스 ID (Compose 제외) | snake_case | `ic_alarm`, `color_primary` |

---

## 패키지 구조 규칙

- 레이어 기준으로 분리: `data` / `domain` / `presentation`
- 화면 단위 패키지는 `presentation` 하위에 기능명으로 생성
  - 예: `presentation/notice/`, `presentation/alarm/`
- 공통 컴포넌트는 `presentation/component/` 에 모음
- 전역 유틸은 `util/` 에 위치
