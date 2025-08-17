package com.realthon.on.psychoTest.repository;

import com.realthon.on.psychoTest.entity.PsychoChoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PsychoChoiceRepository extends JpaRepository<PsychoChoice, Long> {
    List<PsychoChoice> findByQuestionId(Long questionId);
    List<PsychoChoice> findByQuestionTestTypeId(Long testTypeId);
}
