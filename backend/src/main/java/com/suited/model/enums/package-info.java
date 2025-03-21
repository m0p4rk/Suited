/**
 * 텍사스 홀덤 포커 게임의 핵심 열거형 패키지
 * 
 * 이 패키지는 텍사스 홀덤 포커 게임의 모든 상태와 동작을 정의하는 열거형들을 포함합니다.
 * 
 * 주요 열거형:
 * 
 * 1. GameState - 게임의 진행 상태를 나타냄
 *    - PREFLOP: 프리플랍 (홀카드 배분 후 첫 베팅)
 *    - FLOP: 플랍 (플랍 카드 공개 후 두 번째 베팅)
 *    - TURN: 턴 (턴 카드 공개 후 세 번째 베팅)
 *    - RIVER: 리버 (리버 카드 공개 후 마지막 베팅)
 *    - SHOWDOWN: 쇼다운 (핸드 비교 단계)
 *    - FINISHED: 게임 종료
 * 
 * 2. HandRank - 포커 핸드의 강도를 나타냄 (높은 순서대로)
 *    - ROYAL_FLUSH(10): 로열 플러시 (같은 슈트의 A,K,Q,J,10)
 *    - STRAIGHT_FLUSH(9): 스트레이트 플러시 (같은 슈트의 연속된 5장)
 *    - FOUR_OF_A_KIND(8): 포카드 (같은 숫자 4장)
 *    - FULL_HOUSE(7): 풀하우스 (트리플 + 원페어)
 *    - FLUSH(6): 플러시 (같은 슈트 5장)
 *    - STRAIGHT(5): 스트레이트 (연속된 숫자 5장)
 *    - THREE_OF_A_KIND(4): 트리플 (같은 숫자 3장)
 *    - TWO_PAIR(3): 투페어 (같은 숫자 2장씩 2쌍)
 *    - ONE_PAIR(2): 원페어 (같은 숫자 2장)
 *    - HIGH_CARD(1): 하이카드 (가장 높은 카드)
 * 
 * 3. GameAction - 플레이어가 수행할 수 있는 게임 내 액션
 *    - FOLD: 폴드 (현재 핸드 포기)
 *    - CHECK: 체크 (베팅 없이 턴 넘기기)
 *    - CALL: 콜 (현재 베팅 금액에 맞추기)
 *    - RAISE: 레이즈 (베팅 금액 올리기)
 *    - ALL_IN: 올인 (모든 칩 베팅)
 * 
 * 4. RoomAction - 게임방에서 수행할 수 있는 액션
 *    - JOIN: 게임방 참여
 *    - LEAVE: 게임방 나가기
 *    - START_GAME: 게임 시작
 *    - END_GAME: 게임 종료
 * 
 * 5. CardSuit - 카드의 슈트(무늬)
 *    - HEARTS: 하트 (♥)
 *    - DIAMONDS: 다이아몬드 (♦)
 *    - CLUBS: 클럽 (♣)
 *    - SPADES: 스페이드 (♠)
 * 
 * 6. CardRank - 카드의 숫자 (높은 순서대로)
 *    - ACE: 에이스 (A)
 *    - KING: 킹 (K)
 *    - QUEEN: 퀸 (Q)
 *    - JACK: 잭 (J)
 *    - TEN: 10
 *    - NINE: 9
 *    - EIGHT: 8
 *    - SEVEN: 7
 *    - SIX: 6
 *    - FIVE: 5
 *    - FOUR: 4
 *    - THREE: 3
 *    - TWO: 2
 * 
 * 7. PlayerState - 플레이어의 현재 상태
 *    - ACTIVE: 활성 상태 (베팅 가능)
 *    - FOLDED: 폴드 상태
 *    - ALL_IN: 올인 상태
 *    - LEFT_GAME: 현재 게임에서 나감
 *    - LEFT_ROOM: 게임방에서 나감
 * 
 * 8. UserRole - 사용자의 권한 레벨
 *    - USER: 일반 사용자 (게임 참여, 채팅, 프로필 관리)
 *    - ADMIN: 관리자 (시스템 관리, 사용자 관리, 게임방 관리)
 *    - MODERATOR: 중재자 (채팅 관리, 신고 처리, 게임방 모니터링)
 * 
 * 9. BettingRound - 베팅 라운드 상태
 *    - PREFLOP: 프리플랍 베팅
 *    - FLOP: 플랍 베팅
 *    - TURN: 턴 베팅
 *    - RIVER: 리버 베팅
 * 
 * 10. BetType - 베팅 유형
 *     - SMALL_BLIND: 스몰 블라인드 (기본 베팅의 1/2)
 *     - BIG_BLIND: 빅 블라인드 (기본 베팅)
 *     - CALL: 콜 (이전 베팅과 동일한 금액)
 *     - RAISE: 레이즈 (이전 베팅보다 더 높은 금액)
 *     - ALL_IN: 올인 (모든 칩 베팅)
 */
package com.suited.model.enums; 