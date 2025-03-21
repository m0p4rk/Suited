# Changelog

모든 주요 변경 사항이 이 파일에 기록됩니다.

## [Unreleased]

### Added
- 프로젝트 초기 구조 설정 (2024-03-21 03:00 EDT)
- README.md 작성 (2024-03-21 03:05 EDT)
  - 프로젝트 개요
  - 기술 스택
  - 설치 방법
  - 프로젝트 구조
  - API 문서
  - 개발 상태
- 백엔드 기본 패키지 구조 생성 (2024-03-21 03:10 EDT)
  - config: 설정 클래스
  - controller: REST API 컨트롤러
  - service: 비즈니스 로직
  - repository: 데이터 접근 계층
  - model: 도메인 모델
    - entity: JPA 엔티티
    - dto: 데이터 전송 객체
    - enums: 열거형
  - security: 보안 관련 클래스
  - websocket: 웹소켓 관련 클래스
- 각 패키지에 대한 package-info.java 문서화 (2024-03-21 03:13 EDT)
  - 패키지 목적
  - 포함된 클래스 설명
  - 사용 용도
- CHANGELOG.md 생성 (2024-03-21 03:15 EDT)
- .gitignore 업데이트 (2024-03-21 03:20 EDT)
- Git 브랜치 구조 설정 (2024-03-21 03:25 EDT)
  - develop 브랜치 생성
  - backend 브랜치 생성
  - 초기 커밋 및 푸시
- 도메인 모델 구현 (2024-03-21 08:40 EDT)
  - 열거형(Enums) 구현
    - CardSuit: 카드 슈트 (하트, 다이아몬드, 클럽, 스페이드)
    - CardRank: 카드 숫자 (에이스부터 2까지)
    - GameState: 게임 진행 상태
    - PlayerState: 플레이어 상태
    - UserRole: 사용자 권한
    - GameAction: 게임 액션
    - HandRank: 핸드 강도
    - RoomAction: 게임방 액션
    - BettingRound: 베팅 라운드
    - BetType: 베팅 타입
  - 엔티티(Entities) 구현
    - User: 사용자 정보 및 권한 관리
    - GameRoom: 게임방 설정 및 관리
    - Game: 게임 진행 상태 및 규칙 관리
    - Player: 플레이어 정보 및 게임 참여 관리
    - Card: 카드 정보 및 소유권 관리
  - 엔티티 관계 설정
    - User-GameRoom: 호스트 관계
    - User-Player: 게임 참여 관계
    - GameRoom-Game: 현재 게임 관계
    - Game-Player: 게임 참여자 관계
    - Game-Card: 커뮤니티 카드 관계
    - Player-Card: 홀카드 관계
  - 데이터 타입 및 제약조건 설정
    - 금액: BigDecimal (precision: 10, scale: 2)
    - 시간: LocalDateTime
    - 고유성: username, nickname
    - 필수 필드: nullable = false
  - 비즈니스 로직 메서드 구현
    - 포인트 관리 (User)
    - 게임방 관리 (GameRoom)
    - 게임 진행 관리 (Game)
    - 플레이어 상태 관리 (Player)
    - 카드 관리 (Card)
- 서비스 계층 구현 (2024-03-21 09:00 EDT)
  - GameService: 게임 진행 및 베팅 관리
  - GameRoomService: 게임방 관리
  - PlayerService: 플레이어 관리
  - HandEvaluator: 핸드 평가
  - UserService: 사용자 관리

### Changed
- 프로젝트 구조를 백엔드 중심에서 풀스택으로 변경 (2024-03-21 03:08 EDT)
- README.md에 프론트엔드 관련 내용 추가 (Coming Soon) (2024-03-21 03:08 EDT)
- 엔티티 문서화 개선 (2024-03-21 08:45 EDT)
  - 상세한 클래스 설명 추가
  - 속성 및 관계 설명 추가
  - 메서드 문서화 추가
  - @see 태그를 통한 관련 클래스 참조 추가
- 게임 상태 관리 개선 (2024-03-21 09:15 EDT)
  - GameState에서 WAITING, IN_PROGRESS 상태 제거
  - PlayerState에서 OUT 상태를 LEFT_GAME, LEFT_ROOM으로 분리
  - GameAction에서 게임방 관련 액션을 RoomAction으로 분리
  - BettingRound와 BetType 열거형 추가
- 서비스 계층 문서화 개선 (2024-03-21 09:30 EDT)
  - 각 서비스 클래스의 역할 및 책임 명확화
  - 메서드 문서화 개선
  - 트랜잭션 관리 설명 추가

### Removed
- 불필요한 파일 제거 (2024-03-21 03:00 EDT)
- GameState에서 불필요한 상태 제거 (2024-03-21 09:15 EDT)
  - WAITING
  - IN_PROGRESS
- PlayerState에서 불필요한 상태 제거 (2024-03-21 09:15 EDT)
  - OUT
- GameAction에서 게임방 관련 액션 제거 (2024-03-21 09:15 EDT)

### Fixed
- PlayerService의 increaseBet 메서드 파라미터 누락 수정 (2024-03-21 09:20 EDT)
- HandEvaluator의 핸드 평가 로직 개선 (2024-03-21 09:25 EDT)
  - 중복 카드 제거 로직 추가
  - 스트레이트 평가 시 Ace-low 스트레이트 고려
  - 스트림 연산 최적화

### Security
- 없음

## [0.1.0] - 2024-03-21 03:00 EDT
- 프로젝트 초기화 