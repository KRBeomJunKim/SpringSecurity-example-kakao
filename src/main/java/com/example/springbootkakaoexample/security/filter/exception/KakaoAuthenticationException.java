package com.example.springbootkakaoexample.security.filter.exception;

import org.springframework.security.core.AuthenticationException;

public class KakaoAuthenticationException extends AuthenticationException {
    public KakaoAuthenticationException(String msg) {
        super(msg);
    }
}
