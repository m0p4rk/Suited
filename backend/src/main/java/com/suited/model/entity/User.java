package com.suited.model.entity;

import com.suited.model.enums.UserRole;
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
 * 텍사스 홀덤 포커 게임의 사용자 엔티티
 * 
 * 사용자의 속성:
 * - username: 로그인 아이디 (고유)
 * - password: 비밀번호 (암호화 저장)
 * - nickname: 표시 이름 (고유)
 * - role: 사용자 권한 (USER, ADMIN, MODERATOR)
 * - points: 보유 포인트
 * - createdAt: 계정 생성 시간
 * - lastLoginAt: 마지막 로그인 시간
 * 
 * 관계:
 * - hostedRooms: 사용자가 호스트인 게임방 목록
 * - players: 사용자의 게임 참여 기록 목록
 * 
 * @see com.suited.model.entity.GameRoom
 * @see com.suited.model.entity.Player
 * @see com.suited.model.enums.UserRole
 */
@Entity
@Table(name = "users")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role = UserRole.USER;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal points = BigDecimal.ZERO;

    @OneToMany(mappedBy = "host")
    private List<GameRoom> hostedRooms = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Player> players = new ArrayList<>();

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime lastLoginAt;

    /**
     * 사용자가 호스트인 게임방을 추가
     * @param room 추가할 게임방
     */
    public void addHostedRoom(GameRoom room) {
        hostedRooms.add(room);
        room.setHost(this);
    }

    /**
     * 사용자의 게임 참여 기록을 추가
     * @param player 추가할 플레이어 기록
     */
    public void addPlayer(Player player) {
        players.add(player);
        player.setUser(this);
    }

    /**
     * 사용자의 포인트를 증가
     * @param amount 증가할 금액
     */
    public void addPoints(BigDecimal amount) {
        this.points = this.points.add(amount);
    }

    /**
     * 사용자의 포인트를 감소
     * @param amount 감소할 금액
     */
    public void subtractPoints(BigDecimal amount) {
        if (this.points.compareTo(amount) < 0) {
            throw new IllegalStateException("Insufficient points");
        }
        this.points = this.points.subtract(amount);
    }

    /**
     * 사용자의 마지막 로그인 시간을 업데이트
     */
    public void updateLastLogin() {
        this.lastLoginAt = LocalDateTime.now();
    }

    /**
     * 사용자가 충분한 포인트를 가지고 있는지 확인
     * @param amount 필요한 금액
     * @return 충분한 포인트 보유 여부
     */
    public boolean hasEnoughPoints(BigDecimal amount) {
        return this.points.compareTo(amount) >= 0;
    }

    /**
     * 사용자의 권한을 변경
     * @param newRole 새로운 권한
     */
    public void changeRole(UserRole newRole) {
        this.role = newRole;
    }

    /**
     * 사용자가 관리자인지 확인
     * @return 관리자 여부
     */
    public boolean isAdmin() {
        return role == UserRole.ADMIN;
    }

    /**
     * 사용자가 중재자인지 확인
     * @return 중재자 여부
     */
    public boolean isModerator() {
        return role == UserRole.MODERATOR;
    }
} 