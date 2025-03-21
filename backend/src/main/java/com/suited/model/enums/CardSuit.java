package com.suited.model.enums;

/**
 * 포커 카드의 슈트(무늬)를 나타내는 열거형
 * 
 * 슈트의 우선순위:
 * 1. SPADES (스페이드)
 * 2. HEARTS (하트)
 * 3. DIAMONDS (다이아몬드)
 * 4. CLUBS (클럽)
 * 
 * @see com.suited.model.entity.Card
 * @see com.suited.service.HandEvaluator
 */
public enum CardSuit {
    HEARTS,     // 하트 (♥)
    DIAMONDS,   // 다이아몬드 (♦)
    CLUBS,      // 클럽 (♣)
    SPADES      // 스페이드 (♠)
} 