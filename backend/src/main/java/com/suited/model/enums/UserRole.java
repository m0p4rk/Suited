package com.suited.model.enums;

/**
 * 텍사스 홀덤 포커 게임에서 사용자의 권한 레벨을 나타내는 열거형
 * 
 * 권한 레벨 설명:
 * - USER: 일반 사용자 (게임 참여, 채팅, 프로필 관리)
 * - ADMIN: 관리자 (시스템 관리, 사용자 관리, 게임방 관리)
 * - MODERATOR: 중재자 (채팅 관리, 신고 처리, 게임방 모니터링)
 * 
 * @see com.suited.model.entity.User
 * @see com.suited.config.SecurityConfig
 */
public enum UserRole {
    USER,       // 일반 사용자
    ADMIN,      // 관리자
    MODERATOR   // 중재자
} 