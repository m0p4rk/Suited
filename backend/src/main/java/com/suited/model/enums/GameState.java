package com.suited.model.enums;

/**
 * 텍사스 홀덤 포커 게임의 진행 상태를 나타내는 열거형
 * 
 * 게임 진행 순서:
 * 1. PREFLOP: 첫 베팅 라운드 (홀카드 배분 후)
 * 2. FLOP: 두 번째 베팅 라운드 (플랍 카드 공개 후)
 * 3. TURN: 세 번째 베팅 라운드 (턴 카드 공개 후)
 * 4. RIVER: 마지막 베팅 라운드 (리버 카드 공개 후)
 * 5. SHOWDOWN: 핸드 비교 단계
 * 6. FINISHED: 게임 종료
 * 
 * 상태 설명:
 * - PREFLOP: 첫 베팅 라운드 (홀카드 배분 후)
 * - FLOP: 두 번째 베팅 라운드 (플랍 카드 공개 후)
 * - TURN: 세 번째 베팅 라운드 (턴 카드 공개 후)
 * - RIVER: 마지막 베팅 라운드 (리버 카드 공개 후)
 * - SHOWDOWN: 핸드 비교 단계
 * - FINISHED: 게임 종료
 */
public enum GameState {
    PREFLOP,        // 프리플랍
    FLOP,           // 플랍
    TURN,           // 턴
    RIVER,          // 리버
    SHOWDOWN,       // 쇼다운
    FINISHED        // 게임 종료
} 