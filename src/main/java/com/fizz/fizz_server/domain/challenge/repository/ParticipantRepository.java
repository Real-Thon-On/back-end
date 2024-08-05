package com.fizz.fizz_server.domain.challenge.repository;

import com.fizz.fizz_server.domain.challenge.domain.Challenge;
import com.fizz.fizz_server.domain.challenge.domain.Participant;
import com.fizz.fizz_server.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ParticipantRepository  extends JpaRepository<Participant, Long> {

    @Query("SELECT COUNT(p) FROM Participant p WHERE p.challenge = :challenge")
    Integer countByChallenge(Challenge challenge);

    @Query("SELECT p.challenge FROM Participant p WHERE p.user = :user")
    List<Challenge> findByUser(User user);

    List<Participant> findByUserAndChallenge(User user, Challenge challenge);

}
