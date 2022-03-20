package com.example.springbootkakaoexample.domain.account;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="oauth_site")
public class Account {

    @Id @GeneratedValue
    private Long id;

    private String username;

    private String password;

    private String nickname;

    private String profileUrl;

    @ElementCollection(fetch = FetchType.LAZY)
    List<String> roles;
}
