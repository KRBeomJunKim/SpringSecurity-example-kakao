package com.example.springbootkakaoexample.domain.account;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@DiscriminatorValue("KAKAO")
public class KakaoAccount extends Account{

    private Long kakaoId;

    @Builder
    public KakaoAccount(Long id, String username, String password, String nickname, String profileUrl, List<String> roles, Long kakaoId) {
        super(id, username, password, nickname, profileUrl, roles);
        this.kakaoId = kakaoId;
    }
}
