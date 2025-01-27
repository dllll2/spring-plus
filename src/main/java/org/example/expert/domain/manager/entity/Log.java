package org.example.expert.domain.manager.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "logs")
public class Log {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private LocalDateTime timestamp;

	private Long todoId;
	private Long managerUserId;

	@Column(nullable = false)
	private String status; // SUCCESS, IN_PROGRESS,  FAILURE

	@Column(length = 500)
	private String message;

	public Log(Long todoId, Long managerUserId, String status, String message) {
		this.timestamp = LocalDateTime.now();
		this.todoId = todoId;
		this.managerUserId = managerUserId;
		this.status = status;
		this.message = message;
	}

}
