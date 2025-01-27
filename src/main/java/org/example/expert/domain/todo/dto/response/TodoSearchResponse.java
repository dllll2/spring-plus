package org.example.expert.domain.todo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TodoSearchResponse {
	private String title;
	private long managerCount;
	private long commentCount;
}
