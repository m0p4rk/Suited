package com.suited.model.entity;

import com.suited.model.enums.PlayerState;
import com.suited.model.enums.BetType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 포커 게임의 플레이어 엔티티
 * 
 * 플레이어의 속성:
 * - betAmount: 현재 베팅 금액
 * - state: 플레이어의 상태 (ACTIVE, FOLDED, ALL_IN, LEFT_GAME, LEFT_ROOM)
 * - position: 테이블에서의 위치 (0부터 시작)
 * - stack: 보유한 칩의 수
 * - isDealer: 딜러 여부
 * - isSmallBlind: 스몰 블라인드 여부
 * - isBigBlind: 빅 블라인드 여부
 * - lastBetType: 마지막 베팅 유형
 * 
 * 관계:
 * - user: 플레이어의 사용자 정보
 * - room: 플레이어가 속한 게임방
 * - game: 플레이어가 참여 중인 게임
 * - cards: 플레이어의 홀카드 목록
 * 
 * @see com.suited.model.enums.PlayerState
 * @see com.suited.model.enums.BetType
 * @see com.suited.model.entity.User
 * @see com.suited.model.entity.GameRoom
 * @see com.suited.model.entity.Game
 * @see com.suited.model.entity.Card
 */
@Entity
@Table(name = "players")
@Getter
@Setter
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private GameRoom room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id")
    private Game game;

    @ElementCollection
    @CollectionTable(name = "player_cards")
    private List<Card> cards = new ArrayList<>();

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal betAmount = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PlayerState state = PlayerState.ACTIVE;

    @Enumerated(EnumType.STRING)
    @Column
    private BetType lastBetType;

    @Column(nullable = false)
    private Integer position;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal stack;

    @Column(nullable = false)
    private boolean isDealer = false;

    @Column(nullable = false)
    private boolean isSmallBlind = false;

    @Column(nullable = false)
    private boolean isBigBlind = false;

    /**
     * 플레이어의 홀카드를 추가
     * @param card 추가할 카드
     */
    public void addCard(Card card) {
        cards.add(card);
        card.setPlayer(this);
    }

    /**
     * 플레이어의 베팅 금액을 증가
     * @param amount 증가할 금액
     * @param betType 베팅 유형
     */
    public void increaseBet(BigDecimal amount, BetType betType) {
        this.betAmount = this.betAmount.add(amount);
        this.stack = this.stack.subtract(amount);
        this.lastBetType = betType;
    }

    /**
     * 플레이어의 상태를 변경
     * @param newState 새로운 상태
     */
    public void changeState(PlayerState newState) {
        this.state = newState;
    }

    /**
     * 플레이어가 올인 상태인지 확인
     * @return 올인 상태 여부
     */
    public boolean isAllIn() {
        return stack.compareTo(BigDecimal.ZERO) == 0;
    }

    /**
     * 플레이어가 폴드 상태인지 확인
     * @return 폴드 상태 여부
     */
    public boolean isFolded() {
        return state == PlayerState.FOLDED;
    }

    /**
     * 플레이어가 게임을 나갔는지 확인
     * @return 게임을 나갔는지 여부
     */
    public boolean hasLeftGame() {
        return state == PlayerState.LEFT_GAME;
    }

    /**
     * 플레이어가 게임방을 나갔는지 확인
     * @return 게임방을 나갔는지 여부
     */
    public boolean hasLeftRoom() {
        return state == PlayerState.LEFT_ROOM;
    }

    /**
     * 플레이어의 카드를 모두 제거
     */
    public void clearCards() {
        cards.clear();
    }
} 