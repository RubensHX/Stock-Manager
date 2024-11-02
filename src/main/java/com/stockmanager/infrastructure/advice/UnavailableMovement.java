package com.stockmanager.infrastructure.advice;

public class UnavailableMovement extends RuntimeException {
    public UnavailableMovement(String message) {
        super(message);
    }
}
