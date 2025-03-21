# 웹 기반 홀덤 사이트 프로젝트 계획서

## 1. 프로젝트 개요

### 1.1 목적
- 실시간 멀티플레이어 홀덤 게임 서비스 제공
- 안정적이고 확장 가능한 게임 서버 구축
- 사용자 경험 최적화

### 1.2 핵심 기능
- 실시간 멀티플레이어 게임
- 사용자 인증 및 프로필 관리
- 게임방 생성 및 관리
- 실시간 채팅
- 랭킹 시스템
- 포인트/머니 시스템

## 2. 시스템 아키텍처

### 2.1 기술 스택
- **백엔드 프레임워크**
  - Spring Boot 3.x
  - Java 17
  - Maven
- **핵심 라이브러리**
  - Spring Security: 인증/인가 처리
  - Spring WebSocket: 실시간 통신
  - Spring Data JPA: 데이터 접근
  - Lombok: 코드 간소화
  - H2 Database: 데이터 저장소
- **개발 도구**
  - IntelliJ IDEA
  - Maven
  - Git

### 2.2 시스템 구성
- **데이터베이스 계층**
  - 인메모리 H2: 실시간 게임 상태 관리
  - 파일 기반 H2: 영구 데이터 저장
- **애플리케이션 계층**
  - REST API 서버
  - WebSocket 서버
  - 게임 로직 서버

### 2.3 시스템 요구사항
- **하드웨어 요구사항**
  - CPU: 2코어 이상
  - RAM: 4GB 이상
  - Storage: 20GB 이상
- **소프트웨어 요구사항**
  - JDK 17 이상
  - Maven 3.8 이상
  - Git 2.30 이상

## 3. 데이터 모델

### 3.1 엔티티 설계
- **User (사용자)**
  ```java
  @Entity
  public class User {
      private Long id;
      private String username;
      private String password;
      private String nickname;
      private Long points;
      private GameStats stats;
      private LocalDateTime createdAt;
      private LocalDateTime lastLoginAt;
      private UserRole role;
  }
  ```

- **GameRoom (게임방)**
  ```java
  @Entity
  public class GameRoom {
      private Long id;
      private String name;
      private User host;
      private List<Player> players;
      private GameState state;
      private GameSettings settings;
      private LocalDateTime createdAt;
      private LocalDateTime lastUpdatedAt;
  }
  ```

- **Player (플레이어)**
  ```java
  @Entity
  public class Player {
      private Long id;
      private User user;
      private List<Card> cards;
      private Long betAmount;
      private PlayerState state;
      private Integer position;
      private Long stack;
  }
  ```

- **Game (게임)**
  ```java
  @Entity
  public class Game {
      private Long id;
      private GameRoom room;
      private List<Card> communityCards;
      private GameRound currentRound;
      private Long pot;
      private List<Player> activePlayers;
      private LocalDateTime startTime;
      private LocalDateTime endTime;
      private Player winner;
  }
  ```

### 3.2 열거형 정의
- **GameState**: WAITING, PLAYING, FINISHED
- **PlayerState**: ACTIVE, FOLDED, ALL_IN
- **GameRound**: PREFLOP, FLOP, TURN, RIVER, SHOWDOWN
- **CardSuit**: HEARTS, DIAMONDS, CLUBS, SPADES
- **CardRank**: ACE, KING, QUEEN, JACK, TEN, NINE, EIGHT, SEVEN, SIX, FIVE, FOUR, THREE, TWO
- **UserRole**: USER, ADMIN, MODERATOR

### 3.3 데이터베이스 스키마
- **테이블 관계**
  - User - GameRoom (1:N)
  - GameRoom - Player (1:N)
  - Player - User (N:1)
  - Game - GameRoom (1:1)
  - Game - Player (1:N)

- **인덱스**
  - User: username (UNIQUE)
  - GameRoom: name, state
  - Player: user_id, game_id
  - Game: room_id, state

## 4. API 설계

### 4.1 REST API
```
/api/v1
├── /auth
│   ├── POST /login
│   ├── POST /register
│   └── POST /logout
├── /users
│   ├── GET /profile
│   ├── GET /stats
│   └── GET /ranking
├── /rooms
│   ├── POST /create
│   ├── POST /join/{roomId}
│   └── GET /list
└── /game
    ├── POST /start
    ├── POST /bet
    └── POST /fold
```

### 4.2 WebSocket API
```
게임 이벤트:
- PLAYER_JOIN: 플레이어 참가
- PLAYER_LEAVE: 플레이어 퇴장
- GAME_START: 게임 시작
- DEAL_CARDS: 카드 분배
- BETTING_ROUND: 배팅 라운드
- SHOWDOWN: 승자 결정
- GAME_END: 게임 종료
```

### 4.3 API 응답 형식
```json
{
    "success": true,
    "data": {},
    "error": null,
    "timestamp": "2024-03-14T12:00:00Z"
}
```

## 5. 게임 로직

### 5.1 카드 관리
- **덱 구성**
  - 52장의 카드 (4슈트 × 13랭크)
  - 셔플링 알고리즘: Fisher-Yates
  - 카드 분배: 시계 방향

### 5.2 게임 진행
1. **프리플랍**
   - 블라인드 베팅
   - 홀 카드 분배
   - 첫 베팅 라운드

2. **플랍**
   - 커뮤니티 카드 3장
   - 두 번째 베팅 라운드

3. **턴**
   - 커뮤니티 카드 1장
   - 세 번째 베팅 라운드

4. **리버**
   - 마지막 커뮤니티 카드
   - 마지막 베팅 라운드

5. **쇼다운**
   - 핸드 조합
   - 승자 결정

### 5.3 핸드 평가
1. 로열 스트레이트 플러시
2. 스트레이트 플러시
3. 포카드
4. 풀하우스
5. 플러시
6. 스트레이트
7. 트리플
8. 투페어
9. 원페어
10. 하이카드

## 6. 프로젝트 구조

### 6.1 패키지 구조
```
com.suited
├── config/          # 설정 클래스
├── controller/      # REST API 컨트롤러
├── service/         # 비즈니스 로직
├── repository/      # 데이터 접근 계층
├── model/          # 도메인 모델
│   ├── entity/     # JPA 엔티티
│   ├── dto/        # 데이터 전송 객체
│   └── enums/      # 열거형
├── security/       # 보안 관련 클래스
└── websocket/      # 웹소켓 관련 클래스
```

### 6.2 주요 클래스
- **Config**
  - WebSocketConfig
  - SecurityConfig
  - DatabaseConfig

- **Controller**
  - AuthController
  - UserController
  - GameRoomController
  - GameController

- **Service**
  - UserService
  - GameRoomService
  - GameService
  - HandEvaluatorService

- **Repository**
  - UserRepository
  - GameRoomRepository
  - GameRepository

## 7. 개발 로드맵

### 7.1 1단계: 환경 구성
- Spring Boot Maven 프로젝트 설정
- H2 데이터베이스 설정
- 개발 환경 구성

### 7.2 2단계: 기본 기능
- 사용자 인증/인가
- WebSocket 설정
- 데이터베이스 스키마

### 7.3 3단계: 게임 로직
- 카드 덱 시스템
- 게임 진행 로직
- 베팅 시스템
- 핸드 평가

### 7.4 4단계: 테스트
- 단위 테스트
- 통합 테스트
- 성능 테스트

### 7.5 5단계: 배포
- AWS 환경 구성
- Docker 컨테이너화
- 데이터베이스 마이그레이션

## 8. 품질 관리

### 8.1 코드 품질
- **코딩 원칙**
  - Clean Code 원칙 적용
  - JavaDoc 주석 필수
  - 의미 있는 변수명과 메서드명 사용
  - 단일 책임 원칙 준수
  - DRY(Don't Repeat Yourself) 원칙 준수

### 8.2 테스트 전략
- **통합 테스트**
  - Spring Test를 활용한 통합 테스트
  - 주요 시나리오별 테스트 케이스 작성
    - 사용자 인증/인가
    - 게임방 생성/참가
    - 게임 진행
    - 베팅 처리
    - 핸드 평가
  - 테스트 데이터 관리
    - 테스트용 데이터베이스 사용
    - 테스트 데이터 초기화 스크립트

### 8.3 모니터링
- **기본 모니터링**
  - Spring Actuator를 통한 기본 모니터링
    - 애플리케이션 상태
    - 메모리 사용량
    - 스레드 상태
  - 로그 관리
    - SLF4J + Logback 사용
    - 로그 레벨별 구분
    - 에러 로그 상세 기록

## 9. 보안

### 9.1 인증/인가
- JWT 기반 인증
- Role 기반 접근 제어
- 세션 관리

### 9.2 데이터 보안
- 비밀번호 암호화
- API 요청 제한
- XSS/CSRF 방지

### 9.3 게임 보안
- 치팅 방지
- 게임 상태 검증
- 트랜잭션 관리

