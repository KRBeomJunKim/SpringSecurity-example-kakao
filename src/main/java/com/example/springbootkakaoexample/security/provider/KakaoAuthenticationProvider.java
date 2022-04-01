package com.example.springbootkakaoexample.security.provider;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.example.springbootkakaoexample.domain.account.KakaoAccount;
import com.example.springbootkakaoexample.security.provider.dto.KakaoTokenResponse;
import com.example.springbootkakaoexample.security.provider.exception.JsonParsingException;
import com.example.springbootkakaoexample.security.service.KakaoAccountDetailsService;
import com.example.springbootkakaoexample.security.service.KakaoWebService;
import com.example.springbootkakaoexample.security.token.KakaoAuthenticationToken;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class KakaoAuthenticationProvider implements AuthenticationProvider {

    private final KakaoAccountDetailsService kakaoUserDetailsService;
    private final KakaoWebService kakaoWebService;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    
        String tokenJson = kakaoWebService.getToken((String)authentication.getPrincipal());
    
        try {
            KakaoTokenResponse kakaoTokenResponse = objectMapper.readValue(tokenJson, KakaoTokenResponse.class);
            String accessToken = kakaoTokenResponse.getAccessToken();

            KakaoAccount account = kakaoUserDetailsService.loadKakaoAccountByAccessToken(accessToken);
            List<SimpleGrantedAuthority> grantedAuthorities = account.getRoles().stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            return new KakaoAuthenticationToken(account, null, grantedAuthorities);
        } catch (JsonProcessingException e) {
            throw new JsonParsingException("Json ParsingError");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(KakaoAuthenticationToken.class);
    }
}
