package com.fizz.fizz_server.domain.user.repository;

import com.fizz.fizz_server.domain.user.domain.User;
import com.fizz.fizz_server.global.oauth2.user.OAuth2Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByProfileId(String profileId);
    Optional<User> findByEmail(String email);
    Optional<User> findByProviderAndProviderId(OAuth2Provider provider, String providerId);

    @Query("SELECT u FROM User u WHERE LOWER(u.nickname) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<User> findByNickname(String keyword);
}
