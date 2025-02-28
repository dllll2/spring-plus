package org.example.expert.domain.todo.repository;

import java.time.LocalDateTime;

import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TodoRepository extends JpaRepository<Todo, Long>, TodoRepositoryCustom {

    // Level 5) 코드 개선 퀴즈 -  JPA의 이해
    @Query("SELECT t FROM Todo t " +
        "LEFT JOIN FETCH t.user u " +
        "WHERE (:weather IS NULL OR t.weather = :weather) " +
        "AND (:startTime IS NULL OR t.modifiedAt >= :startTime) " +
        "AND (:endTime IS NULL OR t.modifiedAt <= :endTime) " +
        "ORDER BY t.modifiedAt DESC")
    Page<Todo> findAllByOrderByModifiedAtDesc(
        @Param("weather") String weather,
        @Param("startTime") LocalDateTime startTime,
        @Param("endTime") LocalDateTime endTime,
        Pageable pageable);
}
