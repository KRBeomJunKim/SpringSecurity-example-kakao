package com.example.springbootkakaoexample.domain.account;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KakaoAccountRepository extends JpaRepository<KakaoAccount, Long> {

    Optional<KakaoAccount> findByKakaoId(Long kakaoId);

}
