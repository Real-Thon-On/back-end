package com.realthon.on.psychoTest.repository;

import com.realthon.on.psychoTest.dto.request.PsychoTestReqeustDto;
import com.realthon.on.psychoTest.entity.PsychoResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PsychoResultRepository extends JpaRepository<PsychoResult, Long> {
    List<PsychoResult> findByUserIdOrderByCreatedAtDesc(Long userId);

}
