package com.example.springbootkakaoexample.security.filter.exception;

import org.springframework.security.core.AuthenticationException;

public class IsNotKakaoLoginRequest extends AuthenticationException {
    public IsNotKakaoLoginRequest(String msg) {
        super(msg);
    }
}
