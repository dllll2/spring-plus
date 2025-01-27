package org.example.expert.domain.manager.service;

import org.example.expert.domain.manager.entity.Log;
import org.example.expert.domain.manager.repository.LogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

// 11 트랜잭션 심화 LOG
@Service
@RequiredArgsConstructor
public class LogService {

	private final LogRepository logRepository;

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void saveLog(Long todoId, Long managerUserId, String status, String message) {
		Log log = new Log(todoId, managerUserId, status, message);
		logRepository.save(log);
	}
}
