package com.suited.model.enums;

/**
 * 텍사스 홀덤 포커의 핸드 강도를 나타내는 열거형
 * 
 * 핸드 강도 순서 (높은 순):
 * 1. ROYAL_FLUSH(10): 같은 슈트의 A-K-Q-J-10
 * 2. STRAIGHT_FLUSH(9): 같은 슈트의 연속된 5장
 * 3. FOUR_OF_A_KIND(8): 같은 숫자 4장
 * 4. FULL_HOUSE(7): 트리플 + 원페어
 * 5. FLUSH(6): 같은 슈트 5장
 * 6. STRAIGHT(5): 연속된 5장
 * 7. THREE_OF_A_KIND(4): 같은 숫자 3장
 * 8. TWO_PAIR(3): 같은 숫자 2장 2쌍
 * 9. ONE_PAIR(2): 같은 숫자 2장
 * 10. HIGH_CARD(1): 가장 높은 카드
 * 
 * @see com.suited.service.HandEvaluator
 */
public enum HandRank {
    ROYAL_FLUSH(10, "로열 플러시"),
    STRAIGHT_FLUSH(9, "스트레이트 플러시"),
    FOUR_OF_A_KIND(8, "포카드"),
    FULL_HOUSE(7, "풀하우스"),
    FLUSH(6, "플러시"),
    STRAIGHT(5, "스트레이트"),
    THREE_OF_A_KIND(4, "트리플"),
    TWO_PAIR(3, "투페어"),
    ONE_PAIR(2, "원페어"),
    HIGH_CARD(1, "하이카드");

    private final int value;
    private final String description;

    HandRank(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public int getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public static HandRank fromValue(int value) {
        for (HandRank rank : values()) {
            if (rank.getValue() == value) {
                return rank;
            }
        }
        throw new IllegalArgumentException("Invalid hand rank value: " + value);
    }
} 