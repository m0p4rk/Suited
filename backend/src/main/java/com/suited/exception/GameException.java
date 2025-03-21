package com.suited.exception;

/**
 * 게임 관련 예외를 처리하는 클래스
 */
public class GameException extends RuntimeException {
    public GameException(String message) {
        super(message);
    }

    public GameException(String message, Throwable cause) {
        super(message, cause);
    }
} 