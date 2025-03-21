/**
 * 텍사스 홀덤 포커 게임의 JPA 엔티티 패키지
 * 
 * 주요 엔티티:
 * 1. User - 사용자 정보
 *    - 기본 정보 (username, password, nickname)
 *    - 권한 관리 (UserRole)
 *    - 포인트 관리
 *    - 게임방 호스트 및 플레이어 관계
 * 
 * 2. GameRoom - 게임방 정보
 *    - 게임방 설정 (최소/최대 플레이어, 블라인드, 바이인)
 *    - 호스트 및 플레이어 관리
 *    - 현재 게임 상태 관리
 * 
 * 3. Game - 게임 진행 정보
 *    - 게임 상태 관리 (GameState)
 *    - 플레이어 순서 및 베팅 관리
 *    - 커뮤니티 카드 관리
 *    - 팟 관리
 * 
 * 4. Player - 게임 참여자 정보
 *    - 플레이어 상태 관리 (PlayerState)
 *    - 홀카드 관리
 *    - 베팅 및 스택 관리
 *    - 포지션 관리
 * 
 * 5. Card - 카드 정보
 *    - 카드 속성 (슈트, 랭크)
 *    - 커뮤니티 카드 여부
 *    - 카드 공개 여부
 *    - 소유자 및 게임 관계
 * 
 * @see com.suited.model.enums.GameState
 * @see com.suited.model.enums.PlayerState
 * @see com.suited.model.enums.UserRole
 * @see com.suited.model.enums.CardSuit
 * @see com.suited.model.enums.CardRank
 */
package com.suited.model.entity; 