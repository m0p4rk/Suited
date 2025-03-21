package com.suited.model.enums;

/**
 * 텍사스 홀덤 포커 게임의 베팅 라운드를 나타내는 열거형
 * 
 * 베팅 라운드 순서:
 * 1. PREFLOP: 홀카드 배분 후 첫 베팅
 * 2. FLOP: 플랍 카드 공개 후 두 번째 베팅
 * 3. TURN: 턴 카드 공개 후 세 번째 베팅
 * 4. RIVER: 리버 카드 공개 후 마지막 베팅
 * 
 * @see com.suited.model.entity.Game
 * @see com.suited.service.GameService
 */
public enum BettingRound {
    PREFLOP,    // 프리플랍 베팅
    FLOP,       // 플랍 베팅
    TURN,       // 턴 베팅
    RIVER       // 리버 베팅
} 