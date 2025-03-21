package com.suited.model.enums;

/**
 * 텍사스 홀덤 포커 게임의 진행 상태를 나타내는 열거형
 * 
 * 게임 진행 순서:
 * 1. WAITING -> STARTING: 게임 시작 전 대기 상태에서 시작 준비로 전환
 * 2. STARTING -> PREFLOP: 플레이어 배치 및 블라인드 설정 후 첫 베팅 라운드 시작
 * 3. PREFLOP -> FLOP: 첫 베팅 라운드 종료 후 플랍 카드 공개
 * 4. FLOP -> TURN: 두 번째 베팅 라운드 종료 후 턴 카드 공개
 * 5. TURN -> RIVER: 세 번째 베팅 라운드 종료 후 리버 카드 공개
 * 6. RIVER -> SHOWDOWN: 마지막 베팅 라운드 종료 후 핸드 비교
 * 7. SHOWDOWN -> ENDED: 승자 결정 및 게임 종료
 */
public enum GameState {
    WAITING,    // 게임 시작 전 대기 상태
    STARTING,   // 게임 시작 준비 단계 (플레이어 배치, 블라인드 설정 등)
    PREFLOP,    // 첫 베팅 라운드 (플레이어의 홀카드만 공개된 상태)
    FLOP,       // 두 번째 베팅 라운드 (플랍 카드 공개)
    TURN,       // 세 번째 베팅 라운드 (턴 카드 공개)
    RIVER,      // 마지막 베팅 라운드 (리버 카드 공개)
    SHOWDOWN,   // 핸드 비교 단계 (모든 플레이어의 핸드 공개 및 비교)
    ENDED       // 게임 종료 상태
} 