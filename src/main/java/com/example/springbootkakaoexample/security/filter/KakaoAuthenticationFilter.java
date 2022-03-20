package com.example.springbootkakaoexample.security.filter;

import com.example.springbootkakaoexample.security.filter.exception.IsNotKakaoLoginRequest;
import com.example.springbootkakaoexample.security.filter.exception.KakaoAuthenticationException;
import com.example.springbootkakaoexample.security.token.KakaoAuthenticationToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class KakaoAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    Logger log = LoggerFactory.getLogger(KakaoAuthenticationFilter.class);

    public KakaoAuthenticationFilter() {
        super(new AntPathRequestMatcher("/auth/login/kakao"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        if (!isKakaoLoginRequest(request)) {
            throw new IsNotKakaoLoginRequest("is not kakao login request.");
        }

        if (hasErrors(request)) {
            throw new KakaoAuthenticationException("Kakao Login Exception");
        }

        String code = request.getParameter("code");
        KakaoAuthenticationToken token = new KakaoAuthenticationToken(code, null);

        return getAuthenticationManager().authenticate(token);
    }

    private boolean hasErrors(HttpServletRequest request) {
        return request.getParameter("error") != null;
    }

    private boolean isKakaoLoginRequest(HttpServletRequest request) {
        return request.getParameter("code") != null;
    }
}
