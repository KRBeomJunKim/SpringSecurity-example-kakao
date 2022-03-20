package com.example.springbootkakaoexample.security.provider;

import com.example.springbootkakaoexample.domain.account.KakaoAccount;
import com.example.springbootkakaoexample.security.provider.dto.KakaoTokenResponse;
import com.example.springbootkakaoexample.security.provider.exception.JsonParsingException;
import com.example.springbootkakaoexample.security.provider.exception.KakaoTimeoutException;
import com.example.springbootkakaoexample.security.service.KakaoAccountDetailsService;
import com.example.springbootkakaoexample.security.token.KakaoAuthenticationToken;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class KakaoAuthenticationProvider implements AuthenticationProvider {

    private final KakaoAccountDetailsService kakaoUserDetailsService;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Value("${kakao.clientId}")
    String clientId;

    @Value("${kakao.redirectUri}")
    String redirectUri;

    @Value("${kakao.clientSecret}")
    String clientSecret;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        MultiValueMap<String, String> bodyMap = new LinkedMultiValueMap<>();
        bodyMap.add("grant_type", "authorization_code");
        bodyMap.add("client_id", clientId);
        bodyMap.add("redirect_uri", redirectUri);
        bodyMap.add("code", (String) authentication.getPrincipal());
        bodyMap.add("client_secret", clientSecret);

        String tokenResponse = WebClient.create("https://kauth.kakao.com")
                .post()
                .uri("/oauth/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(bodyMap))
                .retrieve()
                .bodyToMono(String.class)
                .timeout(Duration.ofMillis(2000), Mono.error(new KakaoTimeoutException("timeout")))
                .block();

        try {
            KakaoTokenResponse kakaoTokenResponse = objectMapper.readValue(tokenResponse, KakaoTokenResponse.class);
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
