package com.suited.service;

import com.suited.model.entity.Card;
import com.suited.model.enums.CardRank;
import com.suited.model.enums.CardSuit;
import com.suited.model.enums.HandRank;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 포커 핸드를 평가하는 클래스
 */
public class HandEvaluator {
    /**
     * 핸드의 강도를 평가합니다.
     * @param cards 평가할 카드들
     * @return 핸드의 강도와 관련 카드들
     */
    public static HandStrength evaluateHand(List<Card> cards) {
        if (cards.size() < 5) {
            throw new IllegalArgumentException("최소 5장의 카드가 필요합니다.");
        }

        // 로열 플러시 체크
        HandStrength royalFlush = checkRoyalFlush(cards);
        if (royalFlush != null) return royalFlush;

        // 스트레이트 플러시 체크
        HandStrength straightFlush = checkStraightFlush(cards);
        if (straightFlush != null) return straightFlush;

        // 포카드 체크
        HandStrength fourOfAKind = checkFourOfAKind(cards);
        if (fourOfAKind != null) return fourOfAKind;

        // 풀하우스 체크
        HandStrength fullHouse = checkFullHouse(cards);
        if (fullHouse != null) return fullHouse;

        // 플러시 체크
        HandStrength flush = checkFlush(cards);
        if (flush != null) return flush;

        // 스트레이트 체크
        HandStrength straight = checkStraight(cards);
        if (straight != null) return straight;

        // 트리플 체크
        HandStrength threeOfAKind = checkThreeOfAKind(cards);
        if (threeOfAKind != null) return threeOfAKind;

        // 투페어 체크
        HandStrength twoPair = checkTwoPair(cards);
        if (twoPair != null) return twoPair;

        // 원페어 체크
        HandStrength onePair = checkOnePair(cards);
        if (onePair != null) return onePair;

        // 하이카드
        return checkHighCard(cards);
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