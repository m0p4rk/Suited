package com.suited.exception;

/**
 * 플레이어 관련 예외를 처리하는 클래스
 */
public class PlayerException extends RuntimeException {
    public PlayerException(String message) {
        super(message);
    }

    public PlayerException(String message, Throwable cause) {
        super(message, cause);
    }
} 