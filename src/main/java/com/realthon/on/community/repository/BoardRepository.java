package com.realthon.on.community.repository;

import com.realthon.on.community.entity.Board;
import com.realthon.on.community.entity.HashTagType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findDistinctByHashtags(HashTagType hashtag);
}

