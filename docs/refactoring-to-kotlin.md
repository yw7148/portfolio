# Refactoring To Kotlin

## 목적

이 문서는 현재 Java 기반 Spring Boot 프로젝트를 Spring Boot 4 기반 Kotlin 백엔드로 전환하기 위한 설계 계획이다. 이제 이 프로젝트는 서버 렌더링 애플리케이션이 아니라, OpenAPI 명세를 기준으로 동작하는 REST API 백엔드로 재구성한다.

핵심 목표:

- Spring Boot `3.2.1` -> Spring Boot `4.0.x`
- Java 중심 코드베이스 -> Kotlin 중심 코드베이스
- Thymeleaf / static page 제공 제거
- OpenAPI YAML 우선 설계
- `openapi-generator`를 이용한 Kotlin/Spring 서버 코드 생성
- 수기 구현 코드와 생성 코드의 경계 분리
- Docker / GitHub Actions 배포 흐름으로 변경

## 새 방향
- Thymeleaf는 제거
- 백엔드는 REST API만 제공
- API 계약은 OpenAPI YAML이 소스 오브 트루스
- Kotlin 구현은 OpenAPI Generator가 만든 서버 인터페이스/모델을 기준으로 작성

즉, 이번 전환은 단순한 Java -> Kotlin 리팩터링이 아니라 아래를 함께 수행하는 아키텍처 전환이다.

1. MVC + 템플릿 서버에서 API 서버로 전환
2. Code-first에서 Contract-first로 전환
3. Java/Lombok 스타일에서 Kotlin + generated contract 스타일로 전환

## 현재 상태

현재 저장소는 다음 특징을 가진다.

- Gradle Groovy DSL 기반 빌드
- Spring Boot `3.2.1`
- Java `17`
- Gradle Wrapper `7.6.1`
- JPA + Thymeleaf + MVC 구조
- Lombok 사용
- 컨트롤러/서비스에서 field injection 사용
- `src/main/resources/templates` 및 `src/main/resources/static`에 렌더링 자산 존재
- Dockerfile과 기존 Jenkins 기반 배포 구성이 현재 빌드 산출물과 메인 클래스 경로에 결합되어 있었음

현재 구조에서 특히 주의할 점:

- `test` 프로필이 `prod-db,prod-server` 그룹을 타고 있어 테스트 안전성이 낮다.
- Oracle 경로와 MariaDB 경로가 분리되어 있어 단순 로컬 확인만으로 완료 판정을 내리기 어렵다.
- Kotlin JPA 전환 시 entity proxy / no-arg / open class 문제가 생길 수 있다.
- 현재 엔드포인트는 HTML 렌더링과 JSON 응답이 섞여 있다.

## 설계 원칙

1. API 계약을 코드보다 먼저 정의한다.
2. 생성 코드는 생성 코드로 남기고, 비즈니스 구현은 수기 코드에 둔다.
3. OpenAPI 스펙 변경 없이 런타임 구현이 임의로 API를 바꾸지 않게 한다.
4. 플랫폼 업그레이드와 구조 전환을 단계적으로 분리한다.
5. MariaDB와 Oracle 두 경로 모두 검증되기 전에는 완료로 간주하지 않는다.

## 권장 전략

권장 전략은 단계적 전환이다.

### 선택한 방향

`Spring Boot 3.5.x 안정화 -> OpenAPI-first 구조 도입 -> Boot 4 + Kotlin generator 체인 도입 -> REST 구현 이전 -> Thymeleaf 제거 -> 최종 검증`

이 경로를 선택한 이유:

- Spring Boot 4 마이그레이션 가이드가 최신 `3.5.x`를 먼저 거치도록 권장한다.
- OpenAPI-first 구조를 먼저 고정해야 이후 generated code와 수기 구현 경계가 안정된다.
- 현재 프로젝트는 코드량보다 배포/DB 경로 리스크가 더 크기 때문에 중간 체크포인트가 중요하다.

## OpenAPI-First 아키텍처

### 소스 오브 트루스

OpenAPI YAML을 API 계약의 유일한 기준으로 둔다.

권장 위치 예시:

- `src/main/openapi/portfolio-api.yaml`

또는

- `openapi/portfolio-api.yaml`

### 코드 생성 전략

권장 생성기:

- OpenAPI Generator `kotlin-spring`

이유:

- 공식 문서 기준 `kotlin-spring`은 stable 상태다.
- Spring Boot 기반 Kotlin 서버 생성에 맞는다.
- `interfaceOnly`, `delegatePattern`, `serviceInterface`, `serviceImplementation` 같은 옵션을 제공해 생성 코드와 수기 구현 코드 분리가 가능하다.

### 권장 생성 방식

이번 프로젝트에서는 아래 패턴을 권장한다.

- generated:
  - request/response model
  - API interface
  - 공통 contract layer
- handwritten:
  - controller implementation 또는 delegate implementation
  - service
  - repository
  - domain rule
  - persistence mapping

가장 안전한 기본안:

- `interfaceOnly=true`
- generated source는 `build/generated/...` 아래에 둠
- 실제 구현은 `src/main/kotlin/...` 에서 generated API interface를 구현

이렇게 하면 생성 결과를 다시 돌려도 수기 구현을 덮어쓰지 않는다.

### 대안

`delegatePattern=true` 또는 `serviceInterface=true`도 가능하다. 다만 기존 프로젝트를 점진적으로 옮기는 상황에서는 생성 코드 overwrite 리스크가 더 낮은 `interfaceOnly` 기반 generation gap 패턴이 기본값으로 더 적합하다.

## 목표 구조

### 남길 것

- Spring Boot REST 백엔드
- JPA 기반 persistence
- MariaDB(local) / Oracle(prod) 지원
- Docker / GitHub Actions 기반 배포

### 제거할 것

- Thymeleaf template 렌더링
- 정적 웹 자산을 백엔드에서 직접 서빙하는 역할
- HTML 페이지 응답을 위한 MVC 경로

### 새로 도입할 것

- OpenAPI YAML
- `openApiValidate`
- `openApiGenerate`
- generated sources를 compile에 포함하는 Gradle 설정
- REST API 응답 중심 테스트

## 제안하는 리소스/API 방향

현재 기능을 기준으로 REST 리소스로 재구성하면 대략 아래 흐름이 자연스럽다.

- `GET /api/v1/projects`
- `GET /api/v1/projects/{projectId}/programs`
- `POST /api/v1/contacts`
- 필요 시 `GET /api/v1/profile` 또는 `GET /api/v1/cv`

이 부분은 프론트엔드 소비 방식에 따라 조정 가능하지만, 핵심 원칙은 다음과 같다.

- HTML 반환 대신 JSON 반환
- `/portfolio`, `/portfolio/cv`, `/` 리다이렉트 같은 페이지 라우팅은 제거 대상
- 현재 뷰모델은 API schema로 재설계

## 단계별 실행 계획

### 1. 3.5.x 안정화 단계

- Gradle wrapper를 Boot 4 호환 버전으로 올린다.
- Spring Boot를 최신 `3.5.x`로 올린다.
- 기존 데이터 접근과 핵심 로직에 대한 회귀 테스트를 추가한다.
- `test` 프로필의 환경 계약을 먼저 정리한다.

완료 기준:

- `3.5.x`에서 빌드와 테스트가 안정적으로 통과한다.
- 기존 도메인 로직을 보호하는 테스트가 존재한다.

### 2. OpenAPI 구조 도입 단계

- OpenAPI YAML 파일 위치를 확정한다.
- 현재 기능을 REST 리소스로 재설계한다.
- request / response schema를 먼저 정의한다.
- CI에 `openApiValidate`를 넣는다.

완료 기준:

- 명세가 유효하다.
- 주요 API 스키마와 응답 코드가 문서화된다.

### 3. Generator 체인 도입 단계

- `openapi-generator` Gradle plugin을 추가한다.
- `openApiGenerate` task를 설정한다.
- `kotlin-spring` generator를 적용한다.
- generated source를 `build/generated` 아래로 분리한다.
- compile 단계가 생성 코드에 의존하도록 구성한다.

완료 기준:

- OpenAPI YAML로부터 Kotlin/Spring 서버 인터페이스와 모델이 생성된다.
- generated source와 수기 구현 source가 분리된다.

### 4. Boot 4 + Kotlin 진입 단계

- Kotlin JVM / Spring / JPA plugin을 도입한다.
- Spring Boot `4.0.x`로 업그레이드한다.
- Boot 4에서 변경된 starter 좌표를 정리한다.
- 애플리케이션 엔트리포인트를 Kotlin으로 전환한다.

완료 기준:

- 애플리케이션이 Boot 4에서 기동한다.
- generator와 Kotlin 빌드가 함께 안정적으로 동작한다.

### 5. REST 구현 단계

- generated API interface를 구현하는 수기 controller/adaptor를 만든다.
- service / repository 계층을 Kotlin으로 이전한다.
- DTO 역할은 generated model과 domain model로 분리 여부를 결정한다.
- field injection을 constructor injection으로 바꾼다.
- Lombok을 제거한다.

완료 기준:

- 핵심 API가 generated contract를 기준으로 구현된다.
- 실행 경로의 핵심 코드가 Kotlin으로 동작한다.

### 6. Thymeleaf 제거 단계

- template 렌더링 controller 제거
- HTML template 제거
- 백엔드에서 더 이상 필요 없는 static asset 정리
- REST API에 맞지 않는 route 제거

완료 기준:

- 애플리케이션이 REST API만 제공한다.
- Thymeleaf 의존성과 템플릿 경로가 제거된다.

### 7. 설정 및 배포 자산 정리

- `application.yml`의 deprecated / suspicious key를 정리한다.
- profile group과 datasource 경로를 점검한다.
- Dockerfile의 build / entrypoint 가정을 새 구조에 맞춘다.
- GitHub Actions의 빌드/릴리스/이미지 배포 흐름을 새 산출물 구조에 맞춘다.
- README를 최신 흐름으로 갱신한다.

완료 기준:

- Docker build가 성공한다.
- GitHub Actions 배포 흐름이 유지된다.

## 핵심 Gradle 설계 포인트

- `openApiValidate`를 명세 품질 게이트로 사용
- `openApiGenerate`를 Kotlin compile 전에 실행
- generated source directory를 명시적으로 추가
- 가능하면 generated code는 커밋하지 않고 빌드 산출물로 관리

권장 원칙:

- 명세 파일은 버전 관리
- generated output은 재생성 가능 상태 유지
- 수기 구현은 generated package와 분리

## 핵심 검증 항목

- OpenAPI YAML validation 통과
- generator 실행 성공
- generated Kotlin/Spring source compile 성공
- `GET /api/v1/projects` 동작 확인
- `GET /api/v1/projects/{projectId}/programs` 동작 확인
- `POST /api/v1/contacts` 동작 확인
- `./gradlew clean test` 통과 여부
- `./gradlew bootJar` 통과 여부
- Docker image build 성공 여부
- Oracle 경로가 별도 환경에서 검증되었는지

## 주요 리스크

### 1. API 재설계 리스크

문제:

- 기존 HTML 중심 엔드포인트를 API 리소스로 바꾸는 과정에서 프론트 소비 계약이 흔들릴 수 있다.

대응:

- OpenAPI YAML을 먼저 작성하고, 그 계약을 기준으로만 구현한다.

### 2. 생성 코드와 수기 코드 충돌

문제:

- generator output이 수기 코드를 덮어쓸 수 있다.

대응:

- generated source를 `build/generated`로 분리하고, generation gap 패턴을 사용한다.

### 3. Kotlin + JPA 호환성

문제:

- Kotlin class는 기본적으로 final이라 JPA proxy 이슈가 생길 수 있다.

대응:

- Kotlin JPA/Spring plugin을 사용하고 integration test를 강화한다.

### 4. 테스트 프로필 신뢰성

문제:

- 현재 `test` 프로필이 `prod-db,prod-server`에 연결돼 있어 회귀 테스트가 환경 의존적일 수 있다.

대응:

- Step 1에서 테스트 계약을 먼저 명확히 한다.

### 5. Docker / CI 결합

문제:

- 메인 클래스와 패키징 구조가 바뀌면 현재 배포 흐름이 깨질 수 있다.

대응:

- 애플리케이션 변경과 같은 단계에서 Docker와 GitHub Actions를 같이 업데이트한다.

## 의사결정 메모

- Thymeleaf는 유지 대상이 아니다.
- 백엔드는 REST API만 남긴다.
- OpenAPI YAML이 구현보다 먼저다.
- OpenAPI Generator는 `kotlin-spring`을 기본 후보로 둔다.
- 기본 생성 전략은 overwrite 리스크가 낮은 generation gap 패턴이다.
- `build.gradle.kts` 전환은 유용하지만, OpenAPI-first 구조 확립보다 우선순위가 높지는 않다.

## 남은 오픈 질문

- API prefix를 `/api/v1`로 고정할지
- generated models를 그대로 domain DTO로 쓸지, 별도 domain model을 둘지
- generated source를 커밋할지, 빌드 산출물로만 둘지
- `interfaceOnly`와 `delegatePattern` 중 어떤 생성 전략을 최종 채택할지
- Oracle 검증을 어느 환경에서 승인할지

## 참고 문서

- Spring Boot System Requirements  
  <https://docs.spring.io/spring-boot/system-requirements.html>
- Spring Boot 4 Migration Guide  
  <https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-4.0-Migration-Guide>
- OpenAPI Generator Plugins  
  <https://openapi-generator.tech/docs/plugins/>
- OpenAPI Generator `kotlin-spring`  
  <https://openapi-generator.tech/docs/generators/kotlin-spring/>
- 내부 실행 계획 PRD  
  `/Users/youngwon/workspace/Portfolio/.omx/plans/prd-spring-boot-4-kotlin-migration.md`
