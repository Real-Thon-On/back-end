package com.fizz.fizz_server.domain.file.repository;

import com.fizz.fizz_server.domain.file.domain.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {
}
