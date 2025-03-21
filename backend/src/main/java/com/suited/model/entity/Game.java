package com.suited.model.entity;

import com.suited.model.enums.GameState;
import com.suited.model.enums.BettingRound;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 텍사스 홀덤 포커 게임 엔티티
 * 
 * 게임의 속성:
 * - pot: 현재 팟의 금액
 * - state: 게임의 진행 상태 (PREFLOP, FLOP, TURN, RIVER, SHOWDOWN, FINISHED)
 * - currentBet: 현재 베팅 라운드의 최고 베팅 금액
 * - currentPlayerIndex: 현재 턴의 플레이어 인덱스
 * - dealerPosition: 딜러 버튼의 위치
 * - smallBlindIndex: 스몰 블라인드의 위치
 * - bigBlindIndex: 빅 블라인드의 위치
 * - lastRaisePosition: 마지막 레이즈한 플레이어의 위치
 * - currentBettingRound: 현재 베팅 라운드
 * 
 * 관계:
 * - room: 게임이 진행되는 게임방
 * - players: 게임에 참여한 플레이어 목록
 * - communityCards: 공개된 커뮤니티 카드 목록 (is_community_card = true인 카드만)
 * 
 * @see com.suited.model.enums.GameState
 * @see com.suited.model.enums.BettingRound
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

    @OneToOne
    @JoinColumn(name = "room_id")
    private GameRoom room;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GameState state = GameState.PREFLOP;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BettingRound currentBettingRound = BettingRound.PREFLOP;

    private BigDecimal currentBet;

    private BigDecimal pot;

    private int dealerPosition;

    private int smallBlindIndex;

    private int bigBlindIndex;

    private int currentPlayerIndex;

    private int lastRaisePosition;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL)
    private List<Player> players = new ArrayList<>();

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL)
    @SQLRestriction("is_community_card = true")
    private List<Card> communityCards = new ArrayList<>();

    /**
     * 게임에 플레이어를 추가
     * @param player 추가할 플레이어
     */
    public void addPlayer(Player player) {
        players.add(player);
    }

    /**
     * 게임에서 플레이어를 제거
     * @param player 제거할 플레이어
     */
    public void removePlayer(Player player) {
        players.remove(player);
    }

    /**
     * 커뮤니티 카드를 추가
     * @param card 추가할 카드
     */
    public void addCommunityCard(Card card) {
        communityCards.add(card);
    }

    /**
     * 커뮤니티 카드를 모두 제거
     */
    public void clearCommunityCards() {
        communityCards.clear();
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
     * 베팅 라운드를 변경
     * @param newRound 새로운 베팅 라운드
     */
    public void changeBettingRound(BettingRound newRound) {
        this.currentBettingRound = newRound;
    }

    /**
     * 다음 플레이어의 턴으로 이동
     */
    public void moveToNextPlayer() {
        // Implementation needed
    }

    /**
     * 딜러 버튼을 다음 플레이어로 이동
     */
    public void moveDealerButton() {
        // Implementation needed
    }

    public void setDealerIndex(int index) {
        this.dealerPosition = index;
        this.smallBlindIndex = (index + 1) % players.size();
        this.bigBlindIndex = (index + 2) % players.size();
    }

    public void setSmallBlindIndex(int index) {
        this.smallBlindIndex = index;
    }

    public void setBigBlindIndex(int index) {
        this.bigBlindIndex = index;
    }

    public void setCurrentPlayerIndex(int index) {
        this.currentPlayerIndex = index;
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public int getSmallBlindIndex() {
        return smallBlindIndex;
    }

    public int getBigBlindIndex() {
        return bigBlindIndex;
    }

    public BigDecimal getBigBlind() {
        return room.getBigBlind();
    }
} 