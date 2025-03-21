/**
 * 텍사스 홀덤 포커 게임의 JPA 엔티티 패키지
 * 
 * 주요 엔티티:
 * 1. User - 사용자 정보
 *    - 기본 정보 (username, password, nickname)
 *    - 권한 관리 (UserRole)
 *    - 포인트 관리 (points)
 *    - 계정 정보 (createdAt, lastLoginAt)
 *    - 게임방 호스트 및 플레이어 관계
 * 
 * 2. GameRoom - 게임방 정보
 *    - 게임방 설정 (name, maxPlayers, minPlayers)
 *    - 블라인드 설정 (smallBlind, bigBlind, buyIn)
 *    - 게임 상태 관리 (GameState)
 *    - 호스트 및 플레이어 관리
 *    - 현재 게임 관리 (currentGame)
 *    - 생성 및 업데이트 시간 관리
 * 
 * 3. Game - 게임 진행 정보
 *    - 게임 상태 관리 (GameState)
 *    - 베팅 라운드 관리 (BettingRound)
 *    - 베팅 관리 (pot, currentBet)
 *    - 포지션 관리 (dealerPosition, smallBlindIndex, bigBlindIndex)
 *    - 플레이어 순서 관리 (currentPlayerIndex)
 *    - 커뮤니티 카드 관리
 *    - 플레이어 목록 관리
 * 
 * 4. Player - 게임 참여자 정보
 *    - 플레이어 상태 관리 (PlayerState)
 *    - 베팅 정보 (betAmount, stack, lastBetType)
 *    - 포지션 정보 (position, isDealer, isSmallBlind, isBigBlind)
 *    - 홀카드 관리
 *    - 사용자, 게임방, 게임 관계 관리
 * 
 * 5. Card - 카드 정보
 *    - 카드 속성 (suit, rank)
 *    - 카드 상태 (isCommunityCard, isVisible)
 *    - 소유자 및 게임 관계 관리
 *    - 카드 비교 및 해시코드 관리
 * 
 * @see com.suited.model.enums.GameState
 * @see com.suited.model.enums.PlayerState
 * @see com.suited.model.enums.UserRole
 * @see com.suited.model.enums.CardSuit
 * @see com.suited.model.enums.CardRank
 * @see com.suited.model.enums.BettingRound
 * @see com.suited.model.enums.BetType
 */
package com.suited.model.entity; 