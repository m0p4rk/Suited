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

### Changed
- 프로젝트 구조를 백엔드 중심에서 풀스택으로 변경 (2024-03-21 03:08 EDT)
- README.md에 프론트엔드 관련 내용 추가 (Coming Soon) (2024-03-21 03:08 EDT)

### Removed
- 불필요한 파일 제거 (2024-03-21 03:00 EDT)

### Fixed
- 없음

### Security
- 없음

## [0.1.0] - 2024-03-21 03:00 EDT
- 프로젝트 초기화 