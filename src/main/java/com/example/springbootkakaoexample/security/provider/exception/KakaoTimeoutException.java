package com.example.springbootkakaoexample.security.provider.exception;

import org.springframework.security.core.AuthenticationException;

import java.util.function.Supplier;

public class KakaoTimeoutException extends AuthenticationException {
    public KakaoTimeoutException(String msg) {
        super(msg);
    }
}
