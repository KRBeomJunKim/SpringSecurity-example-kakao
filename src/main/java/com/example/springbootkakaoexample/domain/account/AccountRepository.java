package com.example.springbootkakaoexample.domain.account;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    @EntityGraph(attributePaths = {"roles"})
    Optional<Account> findByKakaoId(Long kakaoId);
}
