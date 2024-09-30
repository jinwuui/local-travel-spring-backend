# 로컬.xyz Spring Boot 백엔드

## 1. 개요

[로컬.xyz](https://github.com/jinwuui/local-travel-map-frontend) 프로젝트의 Express.js 서버를 Spring Boot로 이전하는 프로젝트 입니다.

## 2. 진행 상황

- 기능 이전 및 테스트 완료
- AWS 배포 진행중

## 3. 기술 스택

Language: Java

Library & Framework: Spring Boot, Spring Security

Database: PostgreSQL, Redis

ORM: JPA(Hibernate)

## 4. 아키텍처

### 디렉토리 구조

```bash
.
├── build.gradle.kts
├── gradlew
├── gradlew.bat
├── settings.gradle.kts
└── src
    ├── main
    │   ├── java
    │   │   └── com.jinwuui.localtravel
    │   │       ├── config                # 설정 관련 파일
    │   │       │   ├── filter
    │   │       │   └── handler
    │   │       ├── controller            # 컨트롤러 (API 엔드포인트)
    │   │       ├── domain                # 엔티티
    │   │       ├── dto                   # 데이터 전송 객체
    │   │       │   ├── mapper            # DTO 매핑
    │   │       │   ├── request           # 요청 DTO
    │   │       │   ├── response          # 응답 DTO
    │   │       │   └── service           # DTO 서비스 객체
    │   │       ├── exception             # 예외 처리
    │   │       ├── repository            # 리포지토리 계층
    │   │       └── service               # 비즈니스 로직
    │   └── resources                     # 리소스 파일
    │       ├── application.yml
    │       └── application-secret.yml
    └── test
        ├── java
        │   └── com.jinwuui.localtravel
        │       ├── config                # 테스트 설정 파일
        │       ├── controller            # 컨트롤러 테스트
        │       ├── security              # 보안 테스트
        │       ├── service               # 서비스 계층 테스트
        │       └── util                  # 유틸리티 테스트
        └── resources
            └── application.yml
```
