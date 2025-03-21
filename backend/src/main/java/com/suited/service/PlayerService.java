package com.suited.service;

import com.suited.exception.GameException;
import com.suited.model.entity.Game;
import com.suited.model.entity.GameRoom;
import com.suited.model.entity.Player;
import com.suited.model.entity.User;
import com.suited.model.enums.PlayerState;
import com.suited.model.enums.BetType;
import com.suited.model.enums.GameState;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * 포커 게임의 플레이어 관련 비즈니스 로직을 처리하는 서비스
 */
@Service
@Transactional
public class PlayerService {
    /**
     * 새로운 플레이어를 생성합니다.
     * @param user 플레이어의 사용자 정보
     * @param room 참여할 게임방
     * @param buyIn 바이인 금액
     * @return 생성된 플레이어
     */
    public Player createPlayer(User user, GameRoom room, BigDecimal buyIn) {
        // 바이인 금액 검증
        if (buyIn.compareTo(room.getBuyIn()) < 0) {
            throw new GameException("바이인 금액이 최소 금액보다 작습니다.");
        }
        if (buyIn.compareTo(user.getPoints()) > 0) {
            throw new GameException("보유한 포인트보다 많은 금액을 바이인할 수 없습니다.");
        }

        // 플레이어 생성
        Player player = new Player();
        player.setUser(user);
        player.setRoom(room);
        player.setStack(buyIn);
        player.setPosition(room.getPlayers().size());
        player.setState(PlayerState.ACTIVE);

        // 사용자의 포인트 차감
        user.subtractPoints(buyIn);

        // 게임방에 플레이어 추가
        room.addPlayer(player);

        return player;
    }

    /**
     * 플레이어를 게임방에서 제거합니다.
     * @param player 제거할 플레이어
     */
    public void removePlayer(Player player) {
        GameRoom room = player.getRoom();
        if (room == null) {
            throw new GameException("플레이어가 게임방에 참여하지 않았습니다.");
        }

        // 게임이 진행 중인 경우
        if (room.getCurrentGame() != null) {
            throw new GameException("게임이 진행 중에는 플레이어를 제거할 수 없습니다.");
        }

        // 플레이어의 남은 스택을 포인트로 반환
        User user = player.getUser();
        user.addPoints(player.getStack());

        // 게임방에서 플레이어 제거
        room.removePlayer(player);
    }

    /**
     * 플레이어의 스택을 증가시킵니다.
     * @param player 대상 플레이어
     * @param amount 증가할 금액
     */
    public void increaseStack(Player player, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new GameException("증가할 금액은 0보다 커야 합니다.");
        }

        player.setStack(player.getStack().add(amount));
    }

    /**
     * 플레이어의 스택을 감소시킵니다.
     * @param player 대상 플레이어
     * @param amount 감소할 금액
     */
    public void decreaseStack(Player player, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new GameException("감소할 금액은 0보다 커야 합니다.");
        }
        if (amount.compareTo(player.getStack()) > 0) {
            throw new GameException("보유한 스택보다 많은 금액을 감소시킬 수 없습니다.");
        }

        player.setStack(player.getStack().subtract(amount));
    }

    /**
     * 플레이어의 베팅 금액을 증가시킵니다.
     * @param player 대상 플레이어
     * @param amount 베팅할 금액
     * @param betType 베팅 타입
     */
    public void increaseBet(Player player, BigDecimal amount, BetType betType) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new GameException("베팅 금액은 0보다 커야 합니다.");
        }
        if (amount.compareTo(player.getStack()) > 0) {
            throw new GameException("보유한 스택보다 많은 금액을 베팅할 수 없습니다.");
        }

        player.increaseBet(amount, betType);
    }

    /**
     * 플레이어의 상태를 변경합니다.
     * @param player 대상 플레이어
     * @param newState 새로운 상태
     */
    public void changeState(Player player, PlayerState newState) {
        player.changeState(newState);
    }

    /**
     * 플레이어의 포지션을 변경합니다.
     * @param player 대상 플레이어
     * @param newPosition 새로운 포지션
     */
    public void changePosition(Player player, int newPosition) {
        Game game = player.getGame();
        if (game == null) {
            throw new GameException("플레이어가 게임에 참여하지 않았습니다.");
        }

        List<Player> players = game.getPlayers();
        if (newPosition < 0 || newPosition >= players.size()) {
            throw new GameException("잘못된 포지션입니다.");
        }

        player.setPosition(newPosition);
    }

    /**
     * 플레이어가 올인 상태인지 확인합니다.
     * @param player 확인할 플레이어
     * @return 올인 상태 여부
     */
    public boolean isAllIn(Player player) {
        return player.isAllIn();
    }

    /**
     * 플레이어가 폴드 상태인지 확인합니다.
     * @param player 확인할 플레이어
     * @return 폴드 상태 여부
     */
    public boolean isFolded(Player player) {
        return player.isFolded();
    }

    /**
     * 플레이어가 게임을 떠납니다.
     * @param player 떠날 플레이어
     */
    public void leaveGame(Player player) {
        Game game = player.getGame();
        if (game == null) {
            throw new GameException("플레이어가 게임에 참여하지 않았습니다.");
        }

        // 게임이 진행 중인 경우
        if (game.getState() != GameState.FINISHED) {
            player.setState(PlayerState.LEFT_GAME);
        } else {
            // 게임이 종료된 경우 게임방에서 제거
            removePlayer(player);
        }
    }

    /**
     * 플레이어가 게임방을 떠납니다.
     * @param player 떠날 플레이어
     */
    public void leaveRoom(Player player) {
        GameRoom room = player.getRoom();
        if (room == null) {
            throw new GameException("플레이어가 게임방에 참여하지 않았습니다.");
        }

        // 게임이 진행 중인 경우
        if (room.getCurrentGame() != null) {
            player.setState(PlayerState.LEFT_ROOM);
        } else {
            // 게임이 진행 중이지 않은 경우 게임방에서 제거
            removePlayer(player);
        }
    }

    /**
     * 플레이어의 베팅 가능 여부를 확인합니다.
     * @param player 확인할 플레이어
     * @param amount 베팅할 금액
     * @param betType 베팅 타입
     * @return 베팅 가능 여부
     */
    public boolean canBet(Player player, BigDecimal amount, BetType betType) {
        if (player.getState() != PlayerState.ACTIVE) {
            return false;
        }
        if (amount.compareTo(player.getStack()) > 0) {
            return false;
        }
        return true;
    }

    /**
     * 플레이어의 올인 가능 여부를 확인합니다.
     * @param player 확인할 플레이어
     * @return 올인 가능 여부
     */
    public boolean canAllIn(Player player) {
        return player.getState() == PlayerState.ACTIVE && player.getStack().compareTo(BigDecimal.ZERO) > 0;
    }

    /**
     * 플레이어의 폴드 가능 여부를 확인합니다.
     * @param player 확인할 플레이어
     * @return 폴드 가능 여부
     */
    public boolean canFold(Player player) {
        return player.getState() == PlayerState.ACTIVE;
    }

    /**
     * 플레이어의 체크 가능 여부를 확인합니다.
     * @param player 확인할 플레이어
     * @return 체크 가능 여부
     */
    public boolean canCheck(Player player) {
        return player.getState() == PlayerState.ACTIVE && player.getBetAmount().compareTo(BigDecimal.ZERO) == 0;
    }

    /**
     * 플레이어의 레이즈 가능 여부를 확인합니다.
     * @param player 확인할 플레이어
     * @param amount 레이즈할 금액
     * @return 레이즈 가능 여부
     */
    public boolean canRaise(Player player, BigDecimal amount) {
        return canBet(player, amount, BetType.RAISE);
    }

    /**
     * 플레이어의 콜 가능 여부를 확인합니다.
     * @param player 확인할 플레이어
     * @param amount 콜할 금액
     * @return 콜 가능 여부
     */
    public boolean canCall(Player player, BigDecimal amount) {
        return canBet(player, amount, BetType.CALL);
    }
} 