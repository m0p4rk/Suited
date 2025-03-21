package com.suited.model.enums;

/**
 * 텍사스 홀덤 포커 게임방에서 수행할 수 있는 액션을 나타내는 열거형
 * 
 * 게임방 관리 액션:
 * - JOIN: 게임방에 참여하기
 * - LEAVE: 게임방에서 나가기
 * - START_GAME: 게임 시작하기
 * - END_GAME: 게임 종료하기
 * 
 * @see com.suited.model.entity.GameRoom
 * @see com.suited.service.GameRoomService
 */
public enum RoomAction {
    JOIN,           // 게임방 참여
    LEAVE,          // 게임방 나가기
    START_GAME,     // 게임 시작
    END_GAME        // 게임 종료
} 