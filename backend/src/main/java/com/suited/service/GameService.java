package com.suited.service;

import com.suited.exception.GameException;
import com.suited.model.entity.Card;
import com.suited.model.entity.Game;
import com.suited.model.entity.Player;
import com.suited.model.enums.CardRank;
import com.suited.model.enums.CardSuit;
import com.suited.model.enums.GameAction;
import com.suited.model.enums.GameState;
import com.suited.model.enums.PlayerState;
import com.suited.model.enums.BetType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 텍사스 홀덤 포커 게임의 비즈니스 로직을 처리하는 서비스
 */
@Service
@Transactional
public class GameService {
    /**
     * 게임을 시작합니다.
     * @param game 시작할 게임
     */
    public void startGame(Game game) {
        if (game.getState() != GameState.PREFLOP) {
            throw new GameException("게임이 이미 시작되었거나 종료되었습니다.");
        }

        // 플레이어 설정
        setupPlayers(game);
        
        // 카드 분배 및 블라인드 게시
        dealInitialCards(game);
        postBlinds(game);
    }

    /**
     * 플레이어의 액션을 처리합니다.
     * @param game 현재 게임
     * @param player 액션을 수행할 플레이어
     * @param action 수행할 액션
     * @param amount 베팅 금액 (레이즈의 경우)
     */
    public void handlePlayerAction(Game game, Player player, GameAction action, BigDecimal amount) {
        validatePlayerTurn(game, player);

        switch (action) {
            case FOLD:
                handleFold(game, player);
                break;
            case CHECK:
                handleCheck(game, player);
                break;
            case CALL:
                handleCall(game, player);
                break;
            case RAISE:
                handleRaise(game, player, amount);
                break;
            case ALL_IN:
                handleAllIn(game, player);
                break;
            default:
                throw new GameException("잘못된 게임 액션입니다.");
        }

        // 베팅 라운드가 끝났는지 확인
        if (isBettingRoundComplete(game)) {
            proceedToNextRound(game);
        }
    }

    /**
     * 게임을 종료합니다.
     * @param game 종료할 게임
     */
    public void endGame(Game game) {
        if (game.getState() == GameState.FINISHED) {
            throw new GameException("게임이 이미 종료되었습니다.");
        }

        // 승자 결정 및 상금 지급
        Player winner = determineWinner(game);
        distributePot(game, winner);

        // 게임 상태 변경
        game.setState(GameState.FINISHED);

        // 플레이어 상태 초기화
        for (Player player : game.getPlayers()) {
            player.setGame(null);
            player.clearCards();
        }
    }

    // Private helper methods

    private void setupPlayers(Game game) {
        List<Player> players = game.getPlayers();
        if (players.size() < 2 || players.size() > 9) {
            throw new GameException("플레이어 수가 올바르지 않습니다.");
        }

        // 플레이어 포지션 설정
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            player.setPosition(i);
            player.setState(PlayerState.ACTIVE);
        }

        // 딜러 버튼 및 블라인드 설정
        game.setDealerIndex(0);
        game.setSmallBlindIndex(1 % players.size());
        game.setBigBlindIndex(2 % players.size());
    }

    private void validatePlayerTurn(Game game, Player player) {
        if (game.getCurrentPlayerIndex() != player.getPosition()) {
            throw new GameException("현재 플레이어의 턴이 아닙니다.");
        }
        if (player.getState() != PlayerState.ACTIVE) {
            throw new GameException("플레이어가 활성 상태가 아닙니다.");
        }
    }

    private void handleFold(Game game, Player player) {
        player.changeState(PlayerState.FOLDED);
        moveToNextPlayer(game);
    }

    private void handleCheck(Game game, Player player) {
        if (game.getCurrentBet().compareTo(BigDecimal.ZERO) > 0) {
            throw new GameException("체크할 수 없는 상황입니다.");
        }
        moveToNextPlayer(game);
    }

    private void handleCall(Game game, Player player) {
        BigDecimal callAmount = game.getCurrentBet().subtract(player.getBetAmount());
        player.increaseBet(callAmount, BetType.CALL);
        game.addToPot(callAmount);
        moveToNextPlayer(game);
    }

    private void handleRaise(Game game, Player player, BigDecimal amount) {
        if (amount.compareTo(game.getCurrentBet()) <= 0) {
            throw new GameException("레이즈 금액이 현재 베팅 금액보다 커야 합니다.");
        }
        if (amount.compareTo(player.getStack()) > 0) {
            throw new GameException("보유한 칩보다 많은 금액을 베팅할 수 없습니다.");
        }

        BigDecimal raiseAmount = amount.subtract(player.getBetAmount());
        player.increaseBet(raiseAmount, BetType.RAISE);
        game.addToPot(raiseAmount);
        game.setCurrentBet(amount);
        game.setCurrentPlayerIndex((game.getCurrentPlayerIndex() + 1) % game.getPlayers().size());
    }

    private void handleAllIn(Game game, Player player) {
        BigDecimal allInAmount = player.getStack();
        player.increaseBet(allInAmount, BetType.ALL_IN);
        game.addToPot(allInAmount);
        if (allInAmount.compareTo(game.getCurrentBet()) > 0) {
            game.setCurrentBet(allInAmount);
            game.setCurrentPlayerIndex((game.getCurrentPlayerIndex() + 1) % game.getPlayers().size());
        } else {
            moveToNextPlayer(game);
        }
    }

    private void moveToNextPlayer(Game game) {
        do {
            game.setCurrentPlayerIndex((game.getCurrentPlayerIndex() + 1) % game.getPlayers().size());
        } while (game.getPlayers().get(game.getCurrentPlayerIndex()).getState() != PlayerState.ACTIVE);
    }

    private boolean isBettingRoundComplete(Game game) {
        List<Player> activePlayers = getActivePlayers(game);
        if (activePlayers.size() <= 1) {
            return true;
        }

        // 모든 활성 플레이어가 동일한 금액을 베팅했는지 확인
        BigDecimal targetBet = game.getCurrentBet();
        return activePlayers.stream()
                .allMatch(player -> player.getBetAmount().compareTo(targetBet) == 0);
    }

    private List<Player> getActivePlayers(Game game) {
        return game.getPlayers().stream()
                .filter(player -> player.getState() == PlayerState.ACTIVE)
                .toList();
    }

    private void proceedToNextRound(Game game) {
        switch (game.getState()) {
            case PREFLOP:
                dealFlop(game);
                game.setState(GameState.FLOP);
                break;
            case FLOP:
                dealTurn(game);
                game.setState(GameState.TURN);
                break;
            case TURN:
                dealRiver(game);
                game.setState(GameState.RIVER);
                break;
            case RIVER:
                game.setState(GameState.SHOWDOWN);
                break;
            default:
                throw new GameException("잘못된 게임 상태입니다.");
        }

        // 새로운 베팅 라운드 시작
        if (game.getState() != GameState.SHOWDOWN) {
            startBettingRound(game);
        }
    }

    private void dealFlop(Game game) {
        for (int i = 0; i < 3; i++) {
            Card card = createDeck().get(0);
            card.setGame(game);
            card.setCommunityCard(true);
            game.addCommunityCard(card);
        }
    }

    private void dealTurn(Game game) {
        Card card = createDeck().get(0);
        card.setGame(game);
        card.setCommunityCard(true);
        game.addCommunityCard(card);
    }

    private void dealRiver(Game game) {
        Card card = createDeck().get(0);
        card.setGame(game);
        card.setCommunityCard(true);
        game.addCommunityCard(card);
    }

    private void distributePot(Game game, Player winner) {
        winner.getStack().add(game.getPot());
        game.setPot(BigDecimal.ZERO);
    }

    private void dealInitialCards(Game game) {
        // 덱 생성 및 셔플
        List<Card> deck = createDeck();
        Collections.shuffle(deck);

        // 홀카드 분배
        for (Player player : game.getPlayers()) {
            for (int i = 0; i < 2; i++) {
                Card card = deck.remove(0);
                card.setPlayer(player);
                player.addCard(card);
            }
        }
    }

    private void postBlinds(Game game) {
        // 스몰 블라인드
        Player smallBlindPlayer = game.getPlayers().get(game.getSmallBlindIndex());
        smallBlindPlayer.increaseBet(game.getRoom().getSmallBlind(), BetType.SMALL_BLIND);

        // 빅 블라인드
        Player bigBlindPlayer = game.getPlayers().get(game.getBigBlindIndex());
        bigBlindPlayer.increaseBet(game.getRoom().getBigBlind(), BetType.BIG_BLIND);

        game.setCurrentBet(game.getRoom().getBigBlind());
        game.setCurrentPlayerIndex((game.getBigBlindIndex() + 1) % game.getPlayers().size());
    }

    private void startBettingRound(Game game) {
        // 빅블라인드부터 시작
        game.setCurrentPlayerIndex(game.getBigBlindIndex());
        game.setCurrentBet(game.getRoom().getBigBlind());
    }

    private List<Card> createDeck() {
        List<Card> deck = new ArrayList<>();
        for (CardSuit suit : CardSuit.values()) {
            for (CardRank rank : CardRank.values()) {
                deck.add(new Card(suit, rank));
            }
        }
        return deck;
    }

    private Player determineWinner(Game game) {
        if (game.getState() != GameState.SHOWDOWN) {
            throw new GameException("쇼다운 단계가 아닙니다.");
        }

        // 폴드하지 않은 플레이어들의 핸드 평가
        List<Player> activePlayers = getActivePlayers(game);
        if (activePlayers.size() == 1) {
            return activePlayers.get(0);
        }

        // 핸드 강도 비교
        Player winner = null;
        HandEvaluator.HandStrength bestHand = null;

        for (Player player : activePlayers) {
            List<Card> playerCards = new ArrayList<>(player.getCards());
            playerCards.addAll(game.getCommunityCards());
            HandEvaluator.HandStrength currentHand = HandEvaluator.evaluateHand(playerCards);

            if (bestHand == null || currentHand.compareTo(bestHand) > 0) {
                bestHand = currentHand;
                winner = player;
            }
        }

        return winner;
    }
} 