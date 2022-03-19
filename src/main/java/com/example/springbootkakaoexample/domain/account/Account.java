package com.example.springbootkakaoexample.domain.account;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Account {

    @Id @GeneratedValue
    private Long id;

    private boolean isKakao;

    private Long kakaoId;

    private String nickname;

    private String profileUrl;

    @ElementCollection(fetch = FetchType.LAZY)
    List<String> roles;

}
