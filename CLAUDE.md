# CLAUDE.md

이 파일은 Claude Code (claude.ai/code)가 이 저장소에서 작업할 때 참고하는 가이드입니다.

## 빌드 및 실행 명령어

```bash
# 전체 프로젝트 빌드
./gradlew build

# admin 모듈 실행 (포트 8080)
./gradlew :admin_module:bootRun

# user 모듈 실행 (포트 8180)
./gradlew :user_module:bootRun

# 테스트 실행
./gradlew test

# 클린 빌드
./gradlew clean build

# QueryDSL Q-클래스 생성 (빌드 시 자동 실행됨)
./gradlew compileQuerydsl
```

참고: admin_module과 user_module의 gradle 설정에서 테스트가 제외되어 있음 (`exclude '**/*'`).

## 아키텍처 개요

**멀티 모듈 Spring Boot 3.1+ 모놀리스** 구조로 3개의 모듈로 구성:

```
my_service/
├── core_module/      # 공유 라이브러리 - 엔티티, 서비스, JWT, 리포지토리
├── admin_module/     # 관리자 포털 (포트 8080)
└── user_module/      # 사용자 포털 (포트 8180)
```

### 모듈 의존성
- `admin_module`과 `user_module`은 모두 `core_module`에 의존
- `core_module`에 모든 공유 비즈니스 로직, 엔티티, 인프라 코드 포함

### core_module 주요 패키지 (ms.toy.my_service)
- `domain/entity/` - JPA 엔티티: Admin, Users, Reservation, Space, Authority, TokenInfo
- `domain/dto/` - 데이터 전송 객체
- `repository/` - Spring Data JPA 리포지토리
- `repository/search/` - QueryDSL 커스텀 검색 리포지토리
- `service/` - 비즈니스 로직 (LoginService, ReservationService, SpaceService, UserService)
- `jwt/` - JWT 인증: TokenProvider, JwtFilter, CustomDetailsService
- `aop/` - `@DistributedLock` 어노테이션을 통한 분산 락 (Redisson/Redis)
- `advice/` - 전역 예외 처리 (CustomExceptionHandler) 및 응답 래핑
- `mapper/` - 엔티티-DTO 변환용 MapStruct 매퍼

### 인증 흐름
- JWT 기반 무상태(stateless) 인증
- Access 토큰 (1시간) + Refresh 토큰 (3시간)
- 역할 기반: ADMIN, USER 역할
- 각 모듈별 CustomDetailsService 구현체 보유

### API 구조
두 모듈 모두 `/api/v1/` 하위에 REST API 제공:
- `/api/v1/login/**` - 인증
- `/api/v1/swagger/index.html` - Swagger UI
- Admin: `/api/v1/admin/**`, `/api/v1/user/**`, `/api/v1/space/**`, `/api/v1/reservation/**`
- User: `/api/v1/my/**`, `/api/v1/reservation/**`, `/api/v1/user/**`

## 기술 스택

- **Java 17**, Spring Boot 3.1.5
- **데이터베이스**: MySQL 8, Spring Data JPA, QueryDSL
- **캐시/락**: Redis (Lettuce), Redisson (분산 락)
- **인증**: JWT (jjwt 라이브러리)
- **API 문서**: SpringDoc OpenAPI 3 (Swagger)
- **유틸리티**: Lombok, MapStruct

## 핵심 패턴

### 분산 락
동시성 제어를 위해 SpEL을 사용한 `@DistributedLock` 어노테이션 사용:
```java
@DistributedLock(key = "'LOCK:' + #date + '-' + #spaceId")
public void saveReservation(String date, Long spaceId, ...) { }
```

### QueryDSL 검색
타입 세이프 쿼리를 위한 커스텀 검색 리포지토리:
- `AdminSearchRepository`, `UserSearchRepository`, `SpaceSearchRepository`, `ReservationSearchRepository`
- 생성된 Q-클래스는 `build/generated/querydsl`에 위치

### 예외 처리
- 표준화된 에러 코드를 위한 `ErrorCode` enum 사용
- core_module의 `CustomExceptionHandler`를 통한 전역 처리

## 설정 파일

- 데이터베이스/Redis 설정: `core_module/src/main/resources/application-core.yml`
- 모듈별 설정: `{module}/src/main/resources/application.yml`
- 활성 프로파일: `local`
