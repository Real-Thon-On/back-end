package com.fizz.fizz_server.security.common.repository;

import com.fizz.fizz_server.security.common.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository  extends JpaRepository<Token, Long> {
    Token findByIdentifier(String identifier);
}