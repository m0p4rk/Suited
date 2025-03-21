package com.suited.model.enums;

/**
 * 포커 카드의 숫자(랭크)를 나타내는 열거형
 * 
 * 랭크의 우선순위 (높은 순):
 * 1. ACE (에이스)
 * 2. KING (킹)
 * 3. QUEEN (퀸)
 * 4. JACK (잭)
 * 5. TEN ~ TWO (10부터 2까지)
 * 
 * @see com.suited.model.entity.Card
 * @see com.suited.service.HandEvaluator
 */
public enum CardRank {
    ACE,        // 에이스 (A)
    KING,       // 킹 (K)
    QUEEN,      // 퀸 (Q)
    JACK,       // 잭 (J)
    TEN,        // 10
    NINE,       // 9
    EIGHT,      // 8
    SEVEN,      // 7
    SIX,        // 6
    FIVE,       // 5
    FOUR,       // 4
    THREE,      // 3
    TWO         // 2
} 