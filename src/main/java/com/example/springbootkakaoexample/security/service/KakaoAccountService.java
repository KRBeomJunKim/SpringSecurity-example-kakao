package com.example.springbootkakaoexample.security.service;

import com.example.springbootkakaoexample.domain.account.Account;
import com.example.springbootkakaoexample.domain.account.AccountRepository;
import com.example.springbootkakaoexample.security.provider.exception.KakaoTimeoutException;
import com.example.springbootkakaoexample.security.service.dto.KakaoAccountResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional
public class KakaoAccountService {

    private final AccountRepository accountRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public Account loadUserByAccessToken(String accessToken) throws UsernameNotFoundException, JsonProcessingException {

        String tokenInformationResponse = WebClient.create("https://kapi.kakao.com")
                .get()
                .uri("/v2/user/me")
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(String.class)
                .timeout(Duration.ofMillis(2000), Mono.error(new KakaoTimeoutException("timeout")))
                .block();

        WebClient.create("https://kapi.kakao.com")
                .get()
                .uri("/v1/user/logout")
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(String.class)
                .timeout(Duration.ofMillis(2000), Mono.error(new KakaoTimeoutException("timeout")))
                .block();

        KakaoAccountResponse kakaoAccountResponse = objectMapper.readValue(tokenInformationResponse, KakaoAccountResponse.class);

        Account account = accountRepository.findByKakaoId(kakaoAccountResponse.getId())
                .orElseGet(() -> {
                    List<String> roles = new ArrayList<>();
                    roles.add("ROLE_USER");

                    return accountRepository.save(Account.builder()
                            .isKakao(true)
                            .kakaoId(kakaoAccountResponse.getId())
                            .nickname(kakaoAccountResponse.getKakaoAccount().getProfile().getNickname())
                            .profileUrl(kakaoAccountResponse.getKakaoAccount().getProfile().getThumbnailImageUrl())
                            .roles(roles)
                            .build());
                });

        return account;
    }
}
