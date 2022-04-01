package com.example.springbootkakaoexample.security.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.springbootkakaoexample.domain.account.KakaoAccount;
import com.example.springbootkakaoexample.domain.account.KakaoAccountRepository;
import com.example.springbootkakaoexample.security.service.dto.KakaoAccountResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Transactional
public class KakaoAccountDetailsService {

    private final KakaoAccountRepository kakaoAccountRepository;
    private final KakaoWebService kakaoWebService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public KakaoAccount loadKakaoAccountByAccessToken(String accessToken) throws UsernameNotFoundException, JsonProcessingException {

        String tokenInformationResponse = kakaoWebService.getAccountInformation(accessToken);
        kakaoWebService.logout(accessToken);

        KakaoAccountResponse kakaoAccountResponse = objectMapper.readValue(tokenInformationResponse, KakaoAccountResponse.class);

        return kakaoAccountRepository.findByKakaoId(kakaoAccountResponse.getId())
                .orElseGet(() -> {
                    List<String> roles = new ArrayList<>();
                    roles.add("ROLE_USER");

                    return kakaoAccountRepository.save(KakaoAccount.builder()
                            .nickname(kakaoAccountResponse.getKakaoAccount().getProfile().getNickname())
                            .profileUrl(kakaoAccountResponse.getKakaoAccount().getProfile().getThumbnailImageUrl())
                            .roles(roles)
                            .kakaoId(kakaoAccountResponse.getId())
                            .build());
                });
    }
}
