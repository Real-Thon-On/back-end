package com.realthon.on.events.repository;

import com.realthon.on.events.entity.LocalArtEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LocalArtEventRepository extends JpaRepository<LocalArtEvent, Long> {

    List<LocalArtEvent> findAllByUrl(String url);

}

