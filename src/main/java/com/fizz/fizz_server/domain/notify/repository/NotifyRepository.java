package com.fizz.fizz_server.domain.notify.repository;

import com.fizz.fizz_server.domain.notify.domain.Notify;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotifyRepository extends JpaRepository<Notify, Long> {
}
