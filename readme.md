# Spring Boot Blog System

이 프로젝트는 Spring Boot를 사용한 기본적인 블로그입니다.
사용자 인증, 게시물 관리 등의 기능을 제공합니다.

## 기능 구성

### 1. 사용자 관리

- 회원가입
- 로그인/로그아웃(세션 기반 인증)
- Spring Security를 통한 인증 처리

### 2. 게시물 관리

- 게시물 목록 조회
- 게시물 상세 조회
- 게시물 작성
- 게시물 수정
- 게시물 삭제

## 시스템 아키텍처

### 전체 시스템 구조

```mermaid
flowchart TB
    subgraph Authentication
        User1[User] --> LoginPage[Login Page]
        User1 --> SignupPage[Signup Page]
        SignupPage -->|POST /user| UserController[UserApiController]
        UserController -->|save| UserSvc[UserService]
        UserSvc -->|save| UserDB[(User DB)]
        LoginPage -->|POST /login| Security[Spring Security]
        Security -->|authenticate| UserDetailSvc[UserDetailService]
        UserDetailSvc --> UserDB
    end

    subgraph BlogManagement
        User2[User] -->|GET /articles| BlogView[BlogViewController]
        User2 -->|GET /new - article| NewArticle[New Article Page]
        NewArticle -->|POST /api/articles| BlogApi[BlogApiController]
        BlogApi -->|save| BlogSvc[BlogService]
        BlogSvc -->|save| ArticleDB[(Article DB)]
        User2 -->|GET /articles/id| ArticleView[Article View]
        ArticleView -->|PUT /api/articles/id| UpdateArticle[Update Article]
        ArticleView -->|DELETE /api/articles/id| DeleteArticle[Delete Article]
        BlogView -->|findAll| BlogSvc
        UpdateArticle --> BlogSvc
        DeleteArticle --> BlogSvc
    end

    subgraph SecurityConfig
        WebSecurity[WebSecurityConfig] -->|configure| FilterChain[Security Filter Chain]
        WebSecurity -->|setup| AuthManager[Authentication Manager]
        WebSecurity -->|setup| PwEncoder[Password Encoder]
    end
```

## 주요 컴포넌트

### Controllers

- `BlogViewController`: 페이지 렌더링 담당
- `BlogApiController`: REST API 엔드포인트 제공
- `UserApiController`: 사용자 관리 API 제공

### Services

- `BlogService`: 게시물 관련 비즈니스 로직
- `UserService`: 사용자 관리 비즈니스 로직
- `UserDetailService`: Spring Security 인증 처리

### Repositories

- `BlogRepository`: 게시물 데이터 관리
- `UserRepository`: 사용자 데이터 관리

## API 엔드포인트

### 블로그 게시물 관련

- `GET /articles`: 게시물 목록 페이지
- `GET /articles/{id}`: 게시물 상세 페이지
- `GET /new-article`: 게시물 작성 페이지
- `POST /api/articles`: 게시물 생성
- `PUT /api/articles/{id}`: 게시물 수정
- `DELETE /api/articles/{id}`: 게시물 삭제

### 사용자 관리 관련

- `GET /login`: 로그인 페이지
- `GET /signup`: 회원가입 페이지
- `POST /user`: 회원가입 처리
- `GET /logout`: 로그아웃 처리

## 기술 스택

- Spring Boot
- Spring Security
- Spring Data JPA
- Thymeleaf
- H2 Database
- Bootstrap
- JavaScript (Fetch API)

## 보안 기능

- BCrypt 비밀번호 암호화
- Spring Security 기반 인증
- 세션 관리

---
## 세션 기반 인증 과정
### 로그인 성공 및 실패시
```mermaid
sequenceDiagram
    actor User
    participant Security as Spring Security
    participant UserDetails as UserDetailsService
    participant DB as Database
    participant Session as Session Storage

    User->>Security: 1. 로그인 요청 (email/password)
    Security->>UserDetails: 2. 사용자 정보 로드 요청
    UserDetails->>DB: 3. DB에서 이메일로 사용자 조회
    DB-->>UserDetails: 4. 사용자 정보 반환
    UserDetails-->>Security: 5. UserDetails 객체 반환
    Security->>Security: 6. 비밀번호 일치 확인
    
    alt 인증 성공
        Security->>Session: 7a. 세션 생성 및 인증 정보 저장
        Session-->>User: 8a. JSESSIONID 쿠키 전송
    else 인증 실패
        Security-->>User: 7b. 인증 실패 응답
    end
```
최초 로그인 시:
- 사용자 정보를 DB에서 조회하고 UserDetails 객체를 생성
- UserDetails 객체를 Security에 전달하여 인증 처리(폼 요청 데이터 비밀번호와 UserDetails 객체의 비밀번호 비교)
- 인증 성공 시 세션을 생성하고 정보를 쿠키에 저장


### 로그인 이후 처리(로그아웃 하지않고 이용)
```mermaid
sequenceDiagram
    actor User
    participant Security as Spring Security
    participant Session as Session Storage

    User->>Security: 1. 요청 + JSESSIONID
    Security->>Session: 2. 세션 유효성 확인
    Session-->>Security: 3. 저장된 인증 정보 반환
    Security->>Security: 4. 인증 정보 확인
    Security-->>User: 5. 요청 처리 결과
```
로그인 이후 요청 시:
- JSESSIONID 쿠키를 통해 저장된 세션 확인
- 세션에 저장된 인정 정보 사용
- DB 조회없이 인증 상태 확인 가능

즉, 세션은 "이미 인증된 사용자임을 증명하는 수단"으로 사용되며, 초기 로그인 시의 인증은 대체하지않습니다.

### 만약 세션및 쿠키, 토큰등을 모두 사용하지 않는 경우
- 매 요청 마다 DB조회와 비밀번호 검증이 필요
- 민감한 정보(ID/PW)를 매번 전송해야함
- DB 조회 및 비밀번호 검증으로 인한 부하 발생
- 로그아웃 기능 구현이 어려움

```mermaid
sequenceDiagram
    actor User
    participant Server
    
    rect rgb(200, 150, 150)
    Note over User,Server: 세션 없이 매 요청마다 인증할 경우
    User->>Server: /articles (ID/PW 전송)
    Server->>Server: DB 조회 및 비밀번호 검증
    User->>Server: /articles/1 (ID/PW 전송)
    Server->>Server: DB 조회 및 비밀번호 검증
    Note over User,Server: 보안 취약: 매번 민감한 인증정보 전송
    end

    rect rgb(150, 200, 150)
    Note over User,Server: 세션 사용할 경우
    User->>Server: 최초 로그인 (ID/PW 전송)
    Server->>Server: DB 조회 및 비밀번호 검증
    Server->>User: 세션 ID 발급
    User->>Server: /articles (세션 ID만 전송)
    Server->>Server: 세션 유효성만 확인
    User->>Server: /articles/1 (세션 ID만 전송)
    Server->>Server: 세션 유효성만 확인
    Note over User,Server: 안전: 민감정보는 최초 1회만 전송
    end
```

### 새로운 계정으로 로그인 시 (세션변경이 일어남)
이 과정에서 이전 세션은 무효화되고 새로운 세션이 생성됩니다.
브라우저의 JSESSIONID 쿠키도 변경됩니다.
```mermaid
sequenceDiagram
    actor User
    participant Browser
    participant Server
    participant Session

    Note over User,Session: 사용자A 로그인
    User->>Browser: userA/pwdA 입력
    Browser->>Server: 로그인 요청
    Server->>Session: 기존 세션 무효화
    Server->>Session: 새 세션 생성 (예: session_A123)
    Server->>Browser: JSESSIONID=session_A123 쿠키 전송
    Browser->>Browser: 쿠키 저장

    Note over User,Session: 사용자B로 변경
    User->>Browser: userB/pwdB 입력
    Browser->>Server: 로그인 요청
    Server->>Session: session_A123 세션 무효화
    Server->>Session: 새 세션 생성 (예: session_B456)
    Server->>Browser: JSESSIONID=session_B456 쿠키 전송
    Browser->>Browser: 새 쿠키로 교체
```