package com.suited.model.enums;

/**
 * 텍사스 홀덤 포커 게임의 베팅 유형을 나타내는 열거형
 * 
 * 베팅 유형:
 * - SMALL_BLIND: 스몰 블라인드 (기본 베팅의 1/2)
 * - BIG_BLIND: 빅 블라인드 (기본 베팅)
 * - CALL: 이전 베팅과 동일한 금액 베팅
 * - RAISE: 이전 베팅보다 더 높은 금액 베팅
 * - ALL_IN: 보유한 모든 칩 베팅
 * 
 * @see com.suited.model.entity.Player
 * @see com.suited.service.GameService
 */
public enum BetType {
    SMALL_BLIND,    // 스몰 블라인드
    BIG_BLIND,      // 빅 블라인드
    CALL,           // 콜
    RAISE,          // 레이즈
    ALL_IN          // 올인
} 