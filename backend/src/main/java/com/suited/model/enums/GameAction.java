package com.suited.model.enums;

/**
 * 텍사스 홀덤 포커 게임에서 플레이어가 수행할 수 있는 게임 내 액션을 나타내는 열거형
 * 
 * 게임 진행 중 액션:
 * - FOLD: 현재 핸드를 포기하고 게임에서 퇴장
 * - CHECK: 베팅이 없을 때 다음 플레이어로 턴을 넘김
 * - CALL: 현재 베팅 금액에 맞춤
 * - RAISE: 현재 베팅 금액보다 더 높게 베팅
 * - ALL_IN: 보유한 모든 칩을 베팅
 * 
 * @see com.suited.model.entity.Player
 * @see com.suited.service.GameService
 */
public enum GameAction {
    FOLD,       // 폴드 (현재 핸드 포기)
    CHECK,      // 체크 (베팅 없이 턴 넘기기)
    CALL,       // 콜 (현재 베팅 금액에 맞추기)
    RAISE,      // 레이즈 (베팅 금액 올리기)
    ALL_IN      // 올인 (모든 칩 베팅)
} 