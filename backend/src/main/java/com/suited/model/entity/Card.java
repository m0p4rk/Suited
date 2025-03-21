package com.suited.model.entity;

import com.suited.model.enums.CardRank;
import com.suited.model.enums.CardSuit;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * 포커 카드 엔티티
 * 
 * 카드의 속성:
 * - suit: 카드의 슈트 (하트, 다이아몬드, 클럽, 스페이드)
 * - rank: 카드의 숫자 (에이스부터 2까지)
 * - isCommunityCard: 커뮤니티 카드 여부 (플랍, 턴, 리버)
 * - isVisible: 카드 공개 여부
 * 
 * 관계:
 * - player: 카드를 소유한 플레이어 (홀카드인 경우)
 * - game: 카드가 속한 게임
 * 
 * @see com.suited.model.enums.CardSuit
 * @see com.suited.model.enums.CardRank
 * @see com.suited.model.entity.Player
 * @see com.suited.model.entity.Game
 */
@Entity
@Table(name = "cards")
@Getter
@Setter
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CardSuit suit;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CardRank rank;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id")
    private Player player;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id")
    private Game game;

    @Column(nullable = false)
    private boolean isCommunityCard = false;

    @Column(nullable = false)
    private boolean isVisible = true;

    public Card() {
    }

    public Card(CardSuit suit, CardRank rank) {
        this.suit = suit;
        this.rank = rank;
    }

    /**
     * 카드의 문자열 표현을 반환
     * 예: "ACE of HEARTS"
     */
    @Override
    public String toString() {
        return rank.name() + " of " + suit.name();
    }

    /**
     * 카드의 숫자 값을 반환
     * 에이스가 가장 높은 값(13)을 가짐
     */
    public int getValue() {
        return rank.ordinal();
    }

    /**
     * 카드의 비교를 위한 equals 메서드
     * 슈트와 랭크가 같으면 같은 카드로 간주
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return suit == card.suit && rank == card.rank;
    }

    /**
     * 카드의 해시코드 계산
     * 슈트와 랭크를 기반으로 계산
     */
    @Override
    public int hashCode() {
        return 31 * suit.hashCode() + rank.hashCode();
    }
} 