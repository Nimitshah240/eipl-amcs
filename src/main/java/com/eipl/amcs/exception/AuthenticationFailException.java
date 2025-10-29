package com.eipl.amcs.exception;

@SuppressWarnings("serial")
public class AuthenticationFailException extends RuntimeException {

    public AuthenticationFailException(Class<?> clazz, String message) {
        super(message);
    }
}
