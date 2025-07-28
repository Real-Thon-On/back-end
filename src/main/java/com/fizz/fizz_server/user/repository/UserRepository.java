package com.fizz.fizz_server.user.repository;


import com.fizz.fizz_server.security.common.enums.ProviderInfo;
import com.fizz.fizz_server.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // 이메일을 기준으로 사용자 찾기
    Optional<User> findByUsername(String username);

    //OAuth 정보( provider 상의 식별자값 , provider enum) 기준으로 사용자 찾기
    @Query("select u from User u where u.identifier = :identifier and u.providerInfo = :providerInfo")
    Optional<User> findByOAuthInfo(@Param("identifier") String identifier,
                                   @Param("providerInfo") ProviderInfo providerInfo);



}