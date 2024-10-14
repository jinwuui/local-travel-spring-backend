# 🗺️ 로-컬.xyz

<br/>

## 1. 개요

기간:

2024.05 ~ 2024.07 (개발 및 배포)

2024.09 ~ 2024.10 (Spring Boot 마이그레이션)

링크: [로-컬.xyz](https://looocal.xyz)

1인 프로젝트

숨겨진 로컬 여행지를 공유하고 탐색하는 플랫폼으로, 사용자들이 경험한 여행지를 서로 나누고 특별한 여행지를 발견할 수 있습니다.

<br/>

## 2. 기술 스택

백엔드: Java / Spring Boot / Spring Security / JPA(Hibernate)

DB: PostgreSQL / Redis

클라우드: AWS EC2 / S3 / Lambda

프론트엔드: Javascript / Vue.js

그 외: NginX / Docker / Jenkins / GitHub Webhooks

<br/>

## 3. 주요 기능

- AWS Lambda를 이용하여 이미지 리사이징 기능 구현
- 여행지 등록 및 조회
  - 사진, 평점, 태그를 포함한 상세한 여행지 정보 작성
  - 태그별 필터링과 검색어 자동 완성 기능으로 사용자가 원하는 여행지를 쉽게 찾을 수 있도록 지원
- Spring Security와 JWT를 활용한 인증 및 인가
  - Access Token과 Refresh Token을 활용한 인증 로직 구현
  - JWT를 사용해 사용자 권한을 관리
- 여행지 북마크 및 구글 장소 연동
  - 여행지를 북마크하여 나중에 쉽게 찾을 수 있는 기능 제공
  - 사용자가 원하는 구글 장소를 바로 등록할 수 있도록 지원

<br/>

## 4. 기술적 도전과 해결 방법

### 4.1. 검색어 자동 완성

- **기존 방식**: 여행지의 제목 또는 내용과 일치하는 초성 및 한/영 철자를 기반으로 검색
- **개선 방식 1**: 기존의 '일치 방식'에 더해, 임베딩(embedding)으로 유사도를 계산하여 검색어를 자동 완성하도록 구현 변경

  - **해결 방법**

    1. 임베딩 값 저장: 여행지를 생성할 때, OpenAI의 텍스트 임베딩 모델로 제목/나라 등의 여행지 정보에 대한 임베딩 값을 계산
    2. 유사도 계산: 사용자가 검색을 수행할 때, 입력된 검색어의 임베딩 값과 저장된 여행지들의 유사도를 계산 (벡터 연산)
    3. 결과 통합: 기존 '일치 방식'의 결과와 유사도 계산 방식의 결과를 합하여 사용자에게 반환

  - **전후 비교**

    - **전 (완전 일치 검색)**

      <img width="300" alt="Screenshot 2024-07-11 at 12 46 31 PM" src="https://github.com/jinwuui/local-travel-map-frontend/assets/97392729/6331d094-c421-4803-a31a-6996d1081630">

    - **후 (유사도 검색)**

      <img width="300" alt="Screenshot 2024-07-11 at 12 46 31 PM" src="https://github.com/jinwuui/local-travel-map-frontend/assets/97392729/67bf6d98-6bc7-4d93-8d47-33150dbd4924">

- **개선 방식 2**: Cache API와 Service Worker로 프론트엔드 성능 최적화

  - 동일 키워드로 재검색 시, 네트워크 요청을 최소화하고 로드 시간을 단축
  - 540ms -> 2ms / 1010ms -> 2ms

    <img width="521" alt="Screenshot 2024-07-12 at 3 18 17 PM" src="https://github.com/user-attachments/assets/cd397ad4-b05f-4949-a39f-d76354d03c4e">

- **개선 방식 3**: 동일 키워드에 대한 결과값을 Redis에 캐싱하여 백엔드 성능 최적화

  - 불필요한 외부 API 요청과 DB 연산을 줄임
  - 600ms -> 2ms / 540 -> 2ms

    <img width="770" alt="스크린샷 2024-07-14 오전 10 29 34" src="https://github.com/user-attachments/assets/231007f3-116d-4489-b7a5-3962fc0f1cf4">

<br/>

### 4.2. 이미지 최적화

- **기존 방식**: 상세 이미지와 썸네일에 2가지 종류를 사용 (원본 / 썸네일)
- **개선 방식**: 상세 이미지와 썸네일에 4가지 종류를 사용 (small, medium, large / 썸네일)
- **세부 설명**:

  1. 여행지 등록 시, 비동기적으로 4가지 종류의 이미지를 생성
  2. 이미지 생성 시, 더 효율적인 이미지 포맷인 WebP로 생성
  3. `<img>` 태그의 `srcset`과 `sizes` 속성을 사용하여 사용자의 화면 크기에 맞춰 이미지를 제공
  4. 정적 파일(이미지, 폰트)에 캐시 설정을 추가

- **전후 비교**:
  - **전**: 원본 전체 24MB, 썸네일 전체 3.3MB
  - **후**: medium 사이즈 전체 2.2MB, 썸네일 전체 483KB
  - **개선 효과**: 상세 이미지 크기 약 11배 감소, 썸네일 크기 약 7배 감소

<br/>

## 5. 아키텍처

### 5.1. 서비스 구조

<img alt="looocal-architecture" src="https://github.com/user-attachments/assets/aadb102e-514b-4653-a68a-1aec66180f8e">

<br/>

### 5.2. 인프라 구조

<img alt="looocal-infra-architecture" src="https://github.com/user-attachments/assets/83fa484c-0f83-4a16-ade3-1f96c36a5b2a">

<br />

### 5.3. 디렉토리 구조

```bash
...
├── local-travel/
│   └── src/
│       ├── main/
│       │   ├── java/
│       │   │   └── com.jinwuui.localtravel/
│       │   │       ├── config/           # 설정 관련 파일
│       │   │       │   ├── filter/       # 보안 필터 (로그인 및 JWT 인증)
│       │   │       │   └── handler/
│       │   │       ├── controller/       # 컨트롤러 (API 엔드포인트)
│       │   │       ├── domain/           # 엔티티
│       │   │       ├── dto/              # 데이터 전송 객체
│       │   │       │   ├── mapper/       # DTO 매핑
│       │   │       │   ├── request/      # 요청 DTO
│       │   │       │   ├── response/     # 응답 DTO
│       │   │       │   └── service/      # DTO 서비스 객체
│       │   │       ├── exception/        # 예외 처리
│       │   │       ├── repository/       # 리포지토리 계층
│       │   │       └── service/          # 비즈니스 로직
│       │   └── resources/                # 리소스 파일
│       │       ├── application.yml
│       │       └── application-secret.yml
│       └── test/
│           ├── java/
│           │   └── com.jinwuui.localtravel/
│           │       ├── config/           # 테스트 설정 파일
│           │       ├── controller/       # 컨트롤러 테스트
│           │       ├── security/         # 보안 테스트
│           │       ├── service/          # 서비스 계층 테스트
│           │       └── util/             # 유틸리티 테스트
│           └── resources/
│               └── application.yml
└── deploy/
    ├── backend/
    │   └── springboot.Dockerfile
    ├── postgresql/
    │   ├── postgresql.Dockerfile
    │   └── vector_extension.sql          # 벡터 확장 설치를 위한 SQL 스크립트
    └── docker-compose.yml
```

<br/>

## 6. UI

| ![ss1](https://github.com/user-attachments/assets/c29c80a1-8bd7-4787-9883-e18d8c5e6f72) | ![ss2](https://github.com/user-attachments/assets/3deccbfa-610b-4e0e-bbae-468c4d5d3811) |
| --------------------------------------------------------------------------------------- | --------------------------------------------------------------------------------------- |

| ![ss3](https://github.com/user-attachments/assets/24399ff4-f95b-4a5b-92d3-21e0218fb898) | ![ss4](https://github.com/user-attachments/assets/aee17791-411a-4dac-8bd8-6ecdaa7ae8b0) |
| --------------------------------------------------------------------------------------- | --------------------------------------------------------------------------------------- |

| ![ss5](https://github.com/user-attachments/assets/702f4602-f793-4232-8501-52f5d24bbb92) | ![ss6](https://github.com/user-attachments/assets/192f0d64-a251-4f62-a43b-5b8f8de6a926) |
| --------------------------------------------------------------------------------------- | --------------------------------------------------------------------------------------- |
