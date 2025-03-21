package com.suited.service;

import com.suited.model.entity.Card;
import com.suited.model.enums.CardRank;
import com.suited.model.enums.CardSuit;
import com.suited.model.enums.HandRank;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 포커 핸드를 평가하는 클래스
 * 
 * 핸드 순위 (높은 순):
 * 1. 로열 플러시 (Royal Flush)
 * 2. 스트레이트 플러시 (Straight Flush)
 * 3. 포카드 (Four of a Kind)
 * 4. 풀하우스 (Full House)
 * 5. 플러시 (Flush)
 * 6. 스트레이트 (Straight)
 * 7. 트리플 (Three of a Kind)
 * 8. 투페어 (Two Pair)
 * 9. 원페어 (One Pair)
 * 10. 하이카드 (High Card)
 */
public class HandEvaluator {
    /**
     * 핸드의 강도를 평가합니다.
     * @param cards 평가할 카드들
     * @return 핸드의 강도와 관련 카드들
     */
    public static HandStrength evaluateHand(List<Card> cards) {
        if (cards == null || cards.size() < 5) {
            throw new IllegalArgumentException("최소 5장의 카드가 필요합니다.");
        }

        // 중복 카드 제거
        List<Card> uniqueCards = cards.stream()
                .distinct()
                .collect(Collectors.toList());

        if (uniqueCards.size() < 5) {
            throw new IllegalArgumentException("중복되지 않은 최소 5장의 카드가 필요합니다.");
        }

        // 로열 플러시 체크
        HandStrength royalFlush = checkRoyalFlush(uniqueCards);
        if (royalFlush != null) return royalFlush;

        // 스트레이트 플러시 체크
        HandStrength straightFlush = checkStraightFlush(uniqueCards);
        if (straightFlush != null) return straightFlush;

        // 포카드 체크
        HandStrength fourOfAKind = checkFourOfAKind(uniqueCards);
        if (fourOfAKind != null) return fourOfAKind;

        // 풀하우스 체크
        HandStrength fullHouse = checkFullHouse(uniqueCards);
        if (fullHouse != null) return fullHouse;

        // 플러시 체크
        HandStrength flush = checkFlush(uniqueCards);
        if (flush != null) return flush;

        // 스트레이트 체크
        HandStrength straight = checkStraight(uniqueCards);
        if (straight != null) return straight;

        // 트리플 체크
        HandStrength threeOfAKind = checkThreeOfAKind(uniqueCards);
        if (threeOfAKind != null) return threeOfAKind;

        // 투페어 체크
        HandStrength twoPair = checkTwoPair(uniqueCards);
        if (twoPair != null) return twoPair;

        // 원페어 체크
        HandStrength onePair = checkOnePair(uniqueCards);
        if (onePair != null) return onePair;

        // 하이카드
        return checkHighCard(uniqueCards);
    }

    /**
     * 두 핸드의 강도를 비교합니다.
     * @param hand1 첫 번째 핸드
     * @param hand2 두 번째 핸드
     * @return 비교 결과 (양수: hand1이 더 강함, 음수: hand2가 더 강함, 0: 동일)
     */
    public static int compareHands(List<Card> hand1, List<Card> hand2) {
        HandStrength strength1 = evaluateHand(hand1);
        HandStrength strength2 = evaluateHand(hand2);
        return strength1.compareTo(strength2);
    }

    /**
     * 핸드의 이름을 반환합니다.
     * @param hand 핸드
     * @return 핸드의 이름
     */
    public static String getHandName(List<Card> hand) {
        HandRank rank = evaluateHand(hand).getRank();
        switch (rank) {
            case ROYAL_FLUSH: return "로열 플러시";
            case STRAIGHT_FLUSH: return "스트레이트 플러시";
            case FOUR_OF_A_KIND: return "포카드";
            case FULL_HOUSE: return "풀하우스";
            case FLUSH: return "플러시";
            case STRAIGHT: return "스트레이트";
            case THREE_OF_A_KIND: return "트리플";
            case TWO_PAIR: return "투페어";
            case ONE_PAIR: return "원페어";
            case HIGH_CARD: return "하이카드";
            default: return "알 수 없는 핸드";
        }
    }

    /**
     * 핸드의 설명을 반환합니다.
     * @param hand 핸드
     * @return 핸드의 설명
     */
    public static String getHandDescription(List<Card> hand) {
        HandStrength strength = evaluateHand(hand);
        List<Card> cards = strength.getCards();
        HandRank rank = strength.getRank();

        switch (rank) {
            case ROYAL_FLUSH:
                return String.format("로열 플러시: %s", cards.get(0).getSuit().toString());
            case STRAIGHT_FLUSH:
                return String.format("스트레이트 플러시: %s %s", cards.get(0).getRank().toString(), cards.get(0).getSuit().toString());
            case FOUR_OF_A_KIND:
                return String.format("포카드: %s", cards.get(0).getRank().toString());
            case FULL_HOUSE:
                return String.format("풀하우스: %s 트리플, %s 페어", 
                    cards.get(0).getRank().toString(), cards.get(3).getRank().toString());
            case FLUSH:
                return String.format("플러시: %s %s", cards.get(0).getSuit().toString(), 
                    cards.stream().map(c -> c.getRank().toString()).collect(Collectors.joining(", ")));
            case STRAIGHT:
                return String.format("스트레이트: %s", 
                    cards.stream().map(c -> c.getRank().toString()).collect(Collectors.joining(" - ")));
            case THREE_OF_A_KIND:
                return String.format("트리플: %s", cards.get(0).getRank().toString());
            case TWO_PAIR:
                return String.format("투페어: %s, %s", 
                    cards.get(0).getRank().toString(), cards.get(2).getRank().toString());
            case ONE_PAIR:
                return String.format("원페어: %s", cards.get(0).getRank().toString());
            case HIGH_CARD:
                return String.format("하이카드: %s", cards.get(0).getRank().toString());
            default:
                return "알 수 없는 핸드";
        }
    }

    private static HandStrength checkRoyalFlush(List<Card> cards) {
        HandStrength straightFlush = checkStraightFlush(cards);
        if (straightFlush != null && straightFlush.getCards().get(0).getRank() == CardRank.ACE) {
            return new HandStrength(HandRank.ROYAL_FLUSH, straightFlush.getCards());
        }
        return null;
    }

    private static HandStrength checkStraightFlush(List<Card> cards) {
        Map<CardSuit, List<Card>> suitGroups = cards.stream()
                .collect(Collectors.groupingBy(Card::getSuit));

        for (List<Card> suitCards : suitGroups.values()) {
            if (suitCards.size() >= 5) {
                HandStrength straight = checkStraight(suitCards);
                if (straight != null) {
                    return new HandStrength(HandRank.STRAIGHT_FLUSH, straight.getCards());
                }
            }
        }
        return null;
    }

    private static HandStrength checkFourOfAKind(List<Card> cards) {
        Map<CardRank, List<Card>> rankGroups = cards.stream()
                .collect(Collectors.groupingBy(Card::getRank));

        for (Map.Entry<CardRank, List<Card>> entry : rankGroups.entrySet()) {
            if (entry.getValue().size() == 4) {
                List<Card> result = new ArrayList<>(entry.getValue());
                cards.stream()
                        .filter(card -> !result.contains(card))
                        .max(Comparator.comparingInt(Card::getValue))
                        .ifPresent(result::add);
                return new HandStrength(HandRank.FOUR_OF_A_KIND, result);
            }
        }
        return null;
    }

    private static HandStrength checkFullHouse(List<Card> cards) {
        Map<CardRank, List<Card>> rankGroups = cards.stream()
                .collect(Collectors.groupingBy(Card::getRank));

        CardRank threeOfAKindRank = null;
        CardRank pairRank = null;

        for (Map.Entry<CardRank, List<Card>> entry : rankGroups.entrySet()) {
            if (entry.getValue().size() == 3 && (threeOfAKindRank == null || 
                    entry.getKey().ordinal() > threeOfAKindRank.ordinal())) {
                threeOfAKindRank = entry.getKey();
            } else if (entry.getValue().size() >= 2 && (pairRank == null || 
                    entry.getKey().ordinal() > pairRank.ordinal())) {
                pairRank = entry.getKey();
            }
        }

        if (threeOfAKindRank != null && pairRank != null) {
            List<Card> result = new ArrayList<>();
            result.addAll(rankGroups.get(threeOfAKindRank).subList(0, 3));
            result.addAll(rankGroups.get(pairRank).subList(0, 2));
            return new HandStrength(HandRank.FULL_HOUSE, result);
        }
        return null;
    }

    private static HandStrength checkFlush(List<Card> cards) {
        Map<CardSuit, List<Card>> suitGroups = cards.stream()
                .collect(Collectors.groupingBy(Card::getSuit));

        for (List<Card> suitCards : suitGroups.values()) {
            if (suitCards.size() >= 5) {
                List<Card> result = suitCards.stream()
                        .sorted(Comparator.comparingInt(Card::getValue).reversed())
                        .limit(5)
                        .collect(Collectors.toList());
                return new HandStrength(HandRank.FLUSH, result);
            }
        }
        return null;
    }

    private static HandStrength checkStraight(List<Card> cards) {
        List<Card> sortedCards = cards.stream()
                .sorted(Comparator.comparingInt(Card::getValue).reversed())
                .distinct()
                .collect(Collectors.toList());

        for (int i = 0; i <= sortedCards.size() - 5; i++) {
            List<Card> potentialStraight = sortedCards.subList(i, i + 5);
            if (isConsecutive(potentialStraight)) {
                return new HandStrength(HandRank.STRAIGHT, potentialStraight);
            }
        }

        // A-2-3-4-5 스트레이트 체크
        Card aceCard = new Card(CardSuit.SPADES, CardRank.ACE);
        if (sortedCards.contains(aceCard)) {
            List<Card> lowStraight = cards.stream()
                    .filter(card -> card.getValue() <= CardRank.FIVE.ordinal())
                    .sorted(Comparator.comparingInt(Card::getValue))
                    .distinct()
                    .limit(4)
                    .collect(Collectors.toList());
            if (lowStraight.size() == 4 && isConsecutive(lowStraight)) {
                lowStraight.add(0, aceCard);
                return new HandStrength(HandRank.STRAIGHT, lowStraight);
            }
        }
        return null;
    }

    private static boolean isConsecutive(List<Card> cards) {
        for (int i = 0; i < cards.size() - 1; i++) {
            if (cards.get(i).getValue() - cards.get(i + 1).getValue() != 1) {
                return false;
            }
        }
        return true;
    }

    private static HandStrength checkThreeOfAKind(List<Card> cards) {
        Map<CardRank, List<Card>> rankGroups = cards.stream()
                .collect(Collectors.groupingBy(Card::getRank));

        for (Map.Entry<CardRank, List<Card>> entry : rankGroups.entrySet()) {
            if (entry.getValue().size() == 3) {
                List<Card> result = new ArrayList<>(entry.getValue());
                cards.stream()
                        .filter(card -> !result.contains(card))
                        .sorted(Comparator.comparingInt(Card::getValue).reversed())
                        .limit(2)
                        .forEach(result::add);
                return new HandStrength(HandRank.THREE_OF_A_KIND, result);
            }
        }
        return null;
    }

    private static HandStrength checkTwoPair(List<Card> cards) {
        Map<CardRank, List<Card>> rankGroups = cards.stream()
                .collect(Collectors.groupingBy(Card::getRank));

        List<CardRank> pairs = rankGroups.entrySet().stream()
                .filter(entry -> entry.getValue().size() >= 2)
                .map(Map.Entry::getKey)
                .sorted(Comparator.comparingInt(CardRank::ordinal).reversed())
                .collect(Collectors.toList());

        if (pairs.size() >= 2) {
            List<Card> result = new ArrayList<>();
            result.addAll(rankGroups.get(pairs.get(0)).subList(0, 2));
            result.addAll(rankGroups.get(pairs.get(1)).subList(0, 2));
            cards.stream()
                    .filter(card -> !result.contains(card))
                    .max(Comparator.comparingInt(Card::getValue))
                    .ifPresent(result::add);
            return new HandStrength(HandRank.TWO_PAIR, result);
        }
        return null;
    }

    private static HandStrength checkOnePair(List<Card> cards) {
        Map<CardRank, List<Card>> rankGroups = cards.stream()
                .collect(Collectors.groupingBy(Card::getRank));

        for (Map.Entry<CardRank, List<Card>> entry : rankGroups.entrySet()) {
            if (entry.getValue().size() == 2) {
                List<Card> result = new ArrayList<>(entry.getValue());
                cards.stream()
                        .filter(card -> !result.contains(card))
                        .sorted(Comparator.comparingInt(Card::getValue).reversed())
                        .limit(3)
                        .forEach(result::add);
                return new HandStrength(HandRank.ONE_PAIR, result);
            }
        }
        return null;
    }

    private static HandStrength checkHighCard(List<Card> cards) {
        List<Card> result = cards.stream()
                .sorted(Comparator.comparingInt(Card::getValue).reversed())
                .limit(5)
                .collect(Collectors.toList());
        return new HandStrength(HandRank.HIGH_CARD, result);
    }

    /**
     * 핸드의 강도를 나타내는 클래스
     */
    public static class HandStrength implements Comparable<HandStrength> {
        private final HandRank rank;
        private final List<Card> cards;

        public HandStrength(HandRank rank, List<Card> cards) {
            this.rank = rank;
            this.cards = cards;
        }

        public HandRank getRank() {
            return rank;
        }

        public List<Card> getCards() {
            return cards;
        }

        @Override
        public int compareTo(HandStrength other) {
            if (this.rank != other.rank) {
                return Integer.compare(this.rank.getValue(), other.rank.getValue());
            }
            for (int i = 0; i < this.cards.size(); i++) {
                int comparison = Integer.compare(
                        this.cards.get(i).getValue(),
                        other.cards.get(i).getValue()
                );
                if (comparison != 0) {
                    return comparison;
                }
            }
            return 0;
        }
    }
} 