package com.realthon.on.community.repository;

import com.realthon.on.community.entity.Board;
import com.realthon.on.community.entity.Like;
import com.realthon.on.user.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends CrudRepository<Like, Long> {
    boolean existsByBoardAndUser(Board board, User user);

    Optional<Like> findByBoardAndUser(Board board, User user);

    long countByBoard(Board board);
}
