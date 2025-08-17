package com.realthon.on.psychoTest.repository;

import com.realthon.on.community.entity.Board;
import com.realthon.on.psychoTest.entity.PsychoQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PsychoQuestionRepository extends JpaRepository<PsychoQuestion, Long> {
    List<PsychoQuestion> findByTestTypeId(Long testTypeId);
}
