package com.fizz.fizz_server.security.jwt.repository;

import com.fizz.fizz_server.security.jwt.entity.RefreshToken;
import com.fizz.fizz_server.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    List<RefreshToken> findAllByUserId(Long userId);


    void deleteAllByUserId(Long userId);

    void deleteByToken(String token);

    Optional<RefreshToken>  findByUserIdAndDeviceId(Long userId, String deviceId);

}