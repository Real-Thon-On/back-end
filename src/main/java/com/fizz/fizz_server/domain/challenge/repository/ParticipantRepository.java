package com.fizz.fizz_server.domain.challenge.repository;

import com.fizz.fizz_server.domain.challenge.domain.Challenge;
import com.fizz.fizz_server.domain.challenge.domain.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface ParticipantRepository  extends JpaRepository<Participant, Long> {

    @Query("SELECT COUNT(p) FROM Participant p WHERE p.challenge = :challenge")
    Integer countByChallenge(Challenge challenge);
}
