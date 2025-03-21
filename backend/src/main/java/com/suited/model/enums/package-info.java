/**
 * Enumerations.
 * Contains all enum classes used throughout the application.
 */
package com.suited.model.enums;

/**
 * 텍사스 홀덤 포커 게임에서 사용되는 열거형 패키지
 * 
 * 주요 열거형:
 * 1. GameState - 게임의 진행 상태를 나타냄
 *    - WAITING: 게임 시작 전 대기 상태
 *    - STARTING: 게임 시작 준비 단계
 *    - PREFLOP: 첫 베팅 라운드
 *    - FLOP: 두 번째 베팅 라운드
 *    - TURN: 세 번째 베팅 라운드
 *    - RIVER: 마지막 베팅 라운드
 *    - SHOWDOWN: 핸드 비교 단계
 *    - ENDED: 게임 종료
 * 
 * 2. HandRank - 포커 핸드의 강도를 나타냄
 *    - ROYAL_FLUSH(10): 로열 플러시
 *    - STRAIGHT_FLUSH(9): 스트레이트 플러시
 *    - FOUR_OF_A_KIND(8): 포카드
 *    - FULL_HOUSE(7): 풀하우스
 *    - FLUSH(6): 플러시
 *    - STRAIGHT(5): 스트레이트
 *    - THREE_OF_A_KIND(4): 트리플
 *    - TWO_PAIR(3): 투페어
 *    - ONE_PAIR(2): 원페어
 *    - HIGH_CARD(1): 하이카드
 * 
 * 3. GameAction - 플레이어가 수행할 수 있는 게임 액션
 *    - FOLD: 폴드
 *    - CHECK: 체크
 *    - CALL: 콜
 *    - RAISE: 레이즈
 *    - ALL_IN: 올인
 *    - LEAVE: 게임방 나가기
 *    - JOIN: 게임 참여
 *    - START: 게임 시작
 *    - END: 게임 종료
 * 
 * 4. CardSuit - 카드의 슈트(무늬)
 *    - HEARTS: 하트
 *    - DIAMONDS: 다이아몬드
 *    - CLUBS: 클럽
 *    - SPADES: 스페이드
 * 
 * 5. CardRank - 카드의 숫자
 *    - ACE: 에이스
 *    - KING: 킹
 *    - QUEEN: 퀸
 *    - JACK: 잭
 *    - TEN ~ TWO: 10부터 2까지
 * 
 * 6. PlayerState - 플레이어의 현재 상태
 *    - ACTIVE: 게임 참여 중
 *    - FOLDED: 폴드
 *    - ALL_IN: 올인
 * 
 * 7. UserRole - 사용자의 권한 레벨
 *    - USER: 일반 사용자
 *    - ADMIN: 관리자
 *    - MODERATOR: 중재자
 */ 