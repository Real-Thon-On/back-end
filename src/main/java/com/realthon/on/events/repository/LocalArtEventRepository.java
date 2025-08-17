package com.realthon.on.events.repository;

import com.realthon.on.events.entity.LocalArtEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LocalArtEventRepository extends JpaRepository<LocalArtEvent, Long> {

    List<LocalArtEvent> findAllByUrl(String url);

    @Query("""
       select e from LocalArtEvent e
       order by case when e.endDate is null then 1 else 0 end asc,
                e.endDate asc
       """)
    List<LocalArtEvent> findSoonestOrderByEndDateNullsLast(org.springframework.data.domain.Pageable pageable);


}

