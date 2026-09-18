package com.trimzo.exception;

public class UrlInactiveException extends RuntimeException {
    public UrlInactiveException(String shortCode) {
        super("This link is no longer active: " + shortCode);
    }
}