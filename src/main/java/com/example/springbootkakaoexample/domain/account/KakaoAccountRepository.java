package com.example.springbootkakaoexample.domain.account;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KakaoAccountRepository extends JpaRepository<KakaoAccount, Long> {

    @EntityGraph(attributePaths = "roles")
    Optional<KakaoAccount> findByKakaoId(Long kakaoId);

}
