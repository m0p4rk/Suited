package com.suited.model.enums;

/**
 * 텍사스 홀덤 포커 게임에서 플레이어의 현재 상태를 나타내는 열거형
 * 
 * 상태 설명:
 * - ACTIVE: 게임에 참여 중이며 베팅 가능한 상태
 * - FOLDED: 현재 핸드를 포기한 상태 (이번 게임에서 더 이상 베팅 불가)
 * - ALL_IN: 모든 칩을 베팅한 상태 (추가 베팅 불가)
 * - LEFT_GAME: 현재 게임에서 나간 상태
 * - LEFT_ROOM: 게임방에서 나간 상태
 * 
 * @see com.suited.model.entity.Player
 * @see com.suited.service.GameService
 */
public enum PlayerState {
    ACTIVE,         // 활성 상태 (베팅 가능)
    FOLDED,         // 폴드 상태
    ALL_IN,         // 올인 상태
    LEFT_GAME,      // 게임에서 나감
    LEFT_ROOM       // 게임방에서 나감
} 