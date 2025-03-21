package com.suited.model.entity;

import com.suited.model.enums.GameState;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 텍사스 홀덤 포커 게임방 엔티티
 * 
 * 게임방의 속성:
 * - name: 게임방 이름
 * - state: 게임방의 상태 (WAITING, STARTING, PREFLOP, FLOP, TURN, RIVER, SHOWDOWN, ENDED)
 * - maxPlayers: 최대 플레이어 수 (기본값: 9)
 * - minPlayers: 최소 플레이어 수 (기본값: 2)
 * - smallBlind: 스몰 블라인드 금액 (기본값: 10)
 * - bigBlind: 빅 블라인드 금액 (기본값: 20)
 * - buyIn: 게임 참여에 필요한 최소 금액 (기본값: 1000)
 * - createdAt: 게임방 생성 시간
 * - lastUpdatedAt: 마지막 업데이트 시간
 * 
 * 관계:
 * - host: 게임방 호스트 (생성자)
 * - players: 게임방에 참여한 플레이어 목록
 * - currentGame: 현재 진행 중인 게임
 * 
 * @see com.suited.model.enums.GameState
 * @see com.suited.model.entity.User
 * @see com.suited.model.entity.Player
 * @see com.suited.model.entity.Game
 */
@Entity
@Table(name = "game_rooms")
@Getter
@Setter
public class GameRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "host_id", nullable = false)
    private User host;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private List<Player> players = new ArrayList<>();

    @OneToOne(mappedBy = "room", cascade = CascadeType.ALL)
    private Game currentGame;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GameState state = GameState.WAITING;

    @Column(nullable = false)
    private Integer maxPlayers = 9;

    @Column(nullable = false)
    private Integer minPlayers = 2;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal smallBlind = new BigDecimal("10");

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal bigBlind = new BigDecimal("20");

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal buyIn = new BigDecimal("1000");

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime lastUpdatedAt;

    /**
     * 게임방에 플레이어를 추가
     * @param player 추가할 플레이어
     * @return 추가 성공 여부
     */
    public boolean addPlayer(Player player) {
        if (players.size() >= maxPlayers) {
            return false;
        }
        players.add(player);
        player.setRoom(this);
        return true;
    }

    /**
     * 게임방에서 플레이어를 제거
     * @param player 제거할 플레이어
     */
    public void removePlayer(Player player) {
        players.remove(player);
        player.setRoom(null);
    }

    /**
     * 게임방의 상태를 변경
     * @param newState 새로운 상태
     */
    public void changeState(GameState newState) {
        this.state = newState;
    }

    /**
     * 게임방이 가득 찼는지 확인
     * @return 가득 찼는지 여부
     */
    public boolean isFull() {
        return players.size() >= maxPlayers;
    }

    /**
     * 게임을 시작할 수 있는지 확인
     * @return 시작 가능 여부
     */
    public boolean canStart() {
        return players.size() >= minPlayers && players.size() <= maxPlayers;
    }

    /**
     * 게임방의 설정을 업데이트
     * @param smallBlind 새로운 스몰 블라인드
     * @param bigBlind 새로운 빅 블라인드
     * @param buyIn 새로운 바이인 금액
     */
    public void updateSettings(BigDecimal smallBlind, BigDecimal bigBlind, BigDecimal buyIn) {
        this.smallBlind = smallBlind;
        this.bigBlind = bigBlind;
        this.buyIn = buyIn;
    }
} 