package com.suited.service;

import com.suited.exception.GameException;
import com.suited.model.entity.Game;
import com.suited.model.entity.GameRoom;
import com.suited.model.entity.Player;
import com.suited.model.entity.User;
import com.suited.model.enums.GameState;
import com.suited.model.enums.PlayerState;
import com.suited.model.enums.BettingRound;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * 포커 게임방 관련 비즈니스 로직을 처리하는 서비스
 */
@Service
@Transactional
public class GameRoomService {
    private final PlayerService playerService;
    private final GameService gameService;

    public GameRoomService(PlayerService playerService, GameService gameService) {
        this.playerService = playerService;
        this.gameService = gameService;
    }

    /**
     * 새로운 게임방을 생성합니다.
     * @param name 게임방 이름
     * @param maxPlayers 최대 플레이어 수
     * @param smallBlind 스몰 블라인드
     * @param bigBlind 빅 블라인드
     * @param buyIn 바이인 금액
     * @param creator 게임방 생성자
     * @return 생성된 게임방
     */
    public GameRoom createRoom(String name, int maxPlayers, BigDecimal smallBlind, 
                             BigDecimal bigBlind, BigDecimal buyIn, User creator) {
        // 게임방 설정 검증
        validateRoomSettings(maxPlayers, smallBlind, bigBlind, buyIn);

        // 게임방 생성
        GameRoom room = new GameRoom();
        room.setName(name);
        room.setMaxPlayers(maxPlayers);
        room.setSmallBlind(smallBlind);
        room.setBigBlind(bigBlind);
        room.setBuyIn(buyIn);
        room.setHost(creator);
        room.changeState(GameState.PREFLOP);

        // 생성자를 첫 번째 플레이어로 추가
        playerService.createPlayer(creator, room, buyIn);

        return room;
    }

    /**
     * 게임방에 플레이어를 참여시킵니다.
     * @param room 참여할 게임방
     * @param user 참여할 사용자
     * @param buyIn 바이인 금액
     * @return 참여한 플레이어
     */
    public Player joinRoom(GameRoom room, User user, BigDecimal buyIn) {
        // 게임방 상태 검증
        validateRoomState(room);

        // 플레이어 수 검증
        if (room.getPlayers().size() >= room.getMaxPlayers()) {
            throw new GameException("게임방이 가득 찼습니다.");
        }

        // 이미 참여한 플레이어인지 검증
        if (isPlayerInRoom(room, user)) {
            throw new GameException("이미 게임방에 참여한 플레이어입니다.");
        }

        // 플레이어 생성 및 참여
        return playerService.createPlayer(user, room, buyIn);
    }

    /**
     * 게임방에서 플레이어를 퇴장시킵니다.
     * @param room 게임방
     * @param user 퇴장할 사용자
     */
    public void leaveRoom(GameRoom room, User user) {
        Player player = findPlayerInRoom(room, user);
        if (player == null) {
            throw new GameException("게임방에 참여하지 않은 플레이어입니다.");
        }

        // 게임이 진행 중인 경우
        if (room.getCurrentGame() != null) {
            player.changeState(PlayerState.LEFT_GAME);
        } else {
            playerService.removePlayer(player);
        }
    }

    /**
     * 게임방의 게임을 시작합니다.
     * @param room 게임방
     */
    public void startGame(GameRoom room) {
        // 게임방 상태 검증
        validateRoomState(room);

        // 플레이어 수 검증
        if (room.getPlayers().size() < 2) {
            throw new GameException("게임을 시작하기 위해서는 최소 2명의 플레이어가 필요합니다.");
        }

        // 게임 생성 및 시작
        Game game = new Game();
        game.setRoom(room);
        game.setState(GameState.PREFLOP);
        game.setCurrentBettingRound(BettingRound.PREFLOP);
        game.setCurrentBet(room.getBigBlind());
        game.setPot(BigDecimal.ZERO);
        game.setDealerPosition(0);

        // 플레이어들을 게임에 참여
        for (Player player : room.getPlayers()) {
            player.setGame(game);
            player.changeState(PlayerState.ACTIVE);
            player.setBetAmount(BigDecimal.ZERO);
        }

        room.setCurrentGame(game);
        gameService.startGame(game);
    }

    /**
     * 게임방의 게임을 종료합니다.
     * @param room 게임방
     */
    public void endGame(GameRoom room) {
        Game game = room.getCurrentGame();
        if (game == null) {
            throw new GameException("진행 중인 게임이 없습니다.");
        }

        gameService.endGame(game);
        room.setCurrentGame(null);
        room.changeState(GameState.PREFLOP);
    }

    /**
     * 게임방의 설정을 변경합니다.
     * @param room 게임방
     * @param maxPlayers 최대 플레이어 수
     * @param smallBlind 스몰 블라인드
     * @param bigBlind 빅 블라인드
     * @param buyIn 바이인 금액
     */
    public void updateRoomSettings(GameRoom room, int maxPlayers, BigDecimal smallBlind,
                                 BigDecimal bigBlind, BigDecimal buyIn) {
        // 게임방 상태 검증
        validateRoomState(room);

        // 게임방 설정 검증
        validateRoomSettings(maxPlayers, smallBlind, bigBlind, buyIn);

        // 플레이어 수 검증
        if (maxPlayers < room.getPlayers().size()) {
            throw new GameException("현재 플레이어 수보다 작은 최대 플레이어 수를 설정할 수 없습니다.");
        }

        // 설정 업데이트
        room.setMaxPlayers(maxPlayers);
        room.setSmallBlind(smallBlind);
        room.setBigBlind(bigBlind);
        room.setBuyIn(buyIn);
    }

    /**
     * 게임방의 상태를 검증합니다.
     * @param room 검증할 게임방
     */
    private void validateRoomState(GameRoom room) {
        if (room == null) {
            throw new GameException("게임방이 존재하지 않습니다.");
        }

        if (room.getCurrentGame() != null) {
            throw new GameException("게임이 진행 중입니다.");
        }
    }

    /**
     * 게임방의 설정을 검증합니다.
     * @param maxPlayers 최대 플레이어 수
     * @param smallBlind 스몰 블라인드
     * @param bigBlind 빅 블라인드
     * @param buyIn 바이인 금액
     */
    private void validateRoomSettings(int maxPlayers, BigDecimal smallBlind,
                                    BigDecimal bigBlind, BigDecimal buyIn) {
        if (maxPlayers < 2 || maxPlayers > 9) {
            throw new GameException("플레이어 수는 2명에서 9명 사이여야 합니다.");
        }

        if (smallBlind.compareTo(BigDecimal.ZERO) <= 0) {
            throw new GameException("스몰 블라인드는 0보다 커야 합니다.");
        }

        if (bigBlind.compareTo(smallBlind) <= 0) {
            throw new GameException("빅 블라인드는 스몰 블라인드보다 커야 합니다.");
        }

        if (buyIn.compareTo(bigBlind.multiply(new BigDecimal("100"))) < 0) {
            throw new GameException("바이인은 빅 블라인드의 100배 이상이어야 합니다.");
        }
    }

    /**
     * 게임방에서 플레이어를 찾습니다.
     * @param room 게임방
     * @param user 사용자
     * @return 찾은 플레이어
     */
    private Player findPlayerInRoom(GameRoom room, User user) {
        return room.getPlayers().stream()
                .filter(p -> p.getUser().getId().equals(user.getId()))
                .findFirst()
                .orElse(null);
    }

    /**
     * 사용자가 게임방에 참여했는지 확인합니다.
     * @param room 게임방
     * @param user 사용자
     * @return 참여 여부
     */
    private boolean isPlayerInRoom(GameRoom room, User user) {
        return findPlayerInRoom(room, user) != null;
    }

    /**
     * 게임방의 플레이어 목록을 반환합니다.
     * @param room 게임방
     * @return 플레이어 목록
     */
    public List<Player> getPlayers(GameRoom room) {
        return room.getPlayers();
    }

    /**
     * 게임방의 현재 게임을 반환합니다.
     * @param room 게임방
     * @return 현재 게임
     */
    public Game getCurrentGame(GameRoom room) {
        return room.getCurrentGame();
    }

    /**
     * 게임방의 호스트를 변경합니다.
     * @param room 게임방
     * @param newHost 새로운 호스트
     */
    public void changeHost(GameRoom room, User newHost) {
        if (!isPlayerInRoom(room, newHost)) {
            throw new GameException("게임방에 참여하지 않은 플레이어를 호스트로 지정할 수 없습니다.");
        }
        room.setHost(newHost);
    }

    /**
     * 게임방의 상태를 변경합니다.
     * @param room 게임방
     * @param newState 새로운 상태
     */
    public void changeRoomState(GameRoom room, GameState newState) {
        validateRoomState(room);
        room.changeState(newState);
    }

    /**
     * 게임방의 플레이어 수를 반환합니다.
     * @param room 게임방
     * @return 플레이어 수
     */
    public int getPlayerCount(GameRoom room) {
        return room.getPlayers().size();
    }

    /**
     * 게임방이 가득 찼는지 확인합니다.
     * @param room 게임방
     * @return 가득 찼는지 여부
     */
    public boolean isRoomFull(GameRoom room) {
        return room.getPlayers().size() >= room.getMaxPlayers();
    }

    /**
     * 게임방의 최소 플레이어 수를 반환합니다.
     * @param room 게임방
     * @return 최소 플레이어 수
     */
    public int getMinPlayers(GameRoom room) {
        return room.getMinPlayers();
    }

    /**
     * 게임방의 최대 플레이어 수를 반환합니다.
     * @param room 게임방
     * @return 최대 플레이어 수
     */
    public int getMaxPlayers(GameRoom room) {
        return room.getMaxPlayers();
    }
} 