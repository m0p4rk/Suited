package com.suited.model.entity;

import com.suited.model.enums.GameState;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 텍사스 홀덤 포커 게임 엔티티
 * 
 * 게임의 속성:
 * - pot: 현재 팟의 금액
 * - state: 게임의 진행 상태 (WAITING, STARTING, PREFLOP, FLOP, TURN, RIVER, SHOWDOWN, ENDED)
 * - currentBet: 현재 베팅 라운드의 최고 베팅 금액
 * - currentPlayerIndex: 현재 턴의 플레이어 인덱스
 * - dealerIndex: 딜러 버튼의 위치
 * - smallBlindIndex: 스몰 블라인드의 위치
 * - bigBlindIndex: 빅 블라인드의 위치
 * - smallBlind: 스몰 블라인드 금액
 * - bigBlind: 빅 블라인드 금액
 * - buyIn: 게임 참여에 필요한 최소 금액
 * 
 * 관계:
 * - room: 게임이 진행되는 게임방
 * - players: 게임에 참여한 플레이어 목록
 * - communityCards: 공개된 커뮤니티 카드 목록
 * 
 * @see com.suited.model.enums.GameState
 * @see com.suited.model.entity.GameRoom
 * @see com.suited.model.entity.Player
 * @see com.suited.model.entity.Card
 */
@Entity
@Table(name = "games")
@Getter
@Setter
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private GameRoom room;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL)
    private List<Player> players = new ArrayList<>();

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL)
    private List<Card> communityCards = new ArrayList<>();

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal pot = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GameState state = GameState.WAITING;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal currentBet = BigDecimal.ZERO;

    @Column(nullable = false)
    private Integer currentPlayerIndex = 0;

    @Column(nullable = false)
    private Integer dealerIndex = 0;

    @Column(nullable = false)
    private Integer smallBlindIndex = 0;

    @Column(nullable = false)
    private Integer bigBlindIndex = 0;

    @Column(nullable = false)
    private Long smallBlind;

    @Column(nullable = false)
    private Long bigBlind;

    @Column(nullable = false)
    private Long buyIn;

    /**
     * 게임에 플레이어를 추가
     * @param player 추가할 플레이어
     */
    public void addPlayer(Player player) {
        players.add(player);
        player.setGame(this);
    }

    /**
     * 커뮤니티 카드를 추가
     * @param card 추가할 카드
     */
    public void addCommunityCard(Card card) {
        communityCards.add(card);
        card.setGame(this);
    }

    /**
     * 팟에 베팅 금액을 추가
     * @param amount 추가할 금액
     */
    public void addToPot(BigDecimal amount) {
        this.pot = this.pot.add(amount);
    }

    /**
     * 현재 베팅 금액을 업데이트
     * @param amount 새로운 베팅 금액
     */
    public void updateCurrentBet(BigDecimal amount) {
        this.currentBet = amount;
    }

    /**
     * 게임의 상태를 변경
     * @param newState 새로운 상태
     */
    public void changeState(GameState newState) {
        this.state = newState;
    }

    /**
     * 다음 플레이어의 턴으로 이동
     */
    public void moveToNextPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }

    /**
     * 딜러 버튼을 다음 플레이어로 이동
     */
    public void moveDealerButton() {
        dealerIndex = (dealerIndex + 1) % players.size();
        smallBlindIndex = (dealerIndex + 1) % players.size();
        bigBlindIndex = (dealerIndex + 2) % players.size();
    }
} 