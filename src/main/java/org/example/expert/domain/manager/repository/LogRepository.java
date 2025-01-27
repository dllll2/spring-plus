package org.example.expert.domain.manager.repository;

import org.example.expert.domain.manager.entity.Log;
import org.springframework.data.jpa.repository.JpaRepository;

// 11 트랜잭션 심화 LOG
public interface LogRepository extends JpaRepository<Log,Long> {
}
