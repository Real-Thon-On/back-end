package com.fizz.fizz_server.domain.challenge.repository;

import com.fizz.fizz_server.domain.challenge.domain.Challenge;
import com.fizz.fizz_server.domain.challenge.domain.Participant;
import com.fizz.fizz_server.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ParticipantRepository  extends JpaRepository<Participant, Long> {

    @Query("SELECT COUNT(p) FROM Participant p WHERE p.challenge = :challenge")
    Integer countByChallenge(Challenge challenge);

    @Query("SELECT p.challenge FROM Participant p WHERE p.user = :user")
    List<Challenge> findByUser(User user);

    Optional<Participant> findFirstByUserAndChallenge(User user, Challenge challenge);

    @Query("select po.id from Post po JOIN Participant pa ON pa.challenge.id = po.challenge.id " +
            "WHERE pa.user.id = :userId")
    List<Long> findPostIdByUserChallenges(@Param("userId") Long userId);

}
