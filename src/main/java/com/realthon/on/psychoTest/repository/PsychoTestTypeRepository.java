package com.realthon.on.psychoTest.repository;

import com.realthon.on.psychoTest.entity.PsychoTestType;
import com.realthon.on.psychoTest.entity.TargetType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface PsychoTestTypeRepository extends JpaRepository<PsychoTestType, Long> {
    List<PsychoTestType> findByTargetTypesIn(Set<TargetType> types);
}
