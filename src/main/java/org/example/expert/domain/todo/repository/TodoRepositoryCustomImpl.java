package org.example.expert.domain.todo.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.example.expert.domain.comment.entity.QComment;
import org.example.expert.domain.manager.entity.QManager;
import org.example.expert.domain.todo.dto.response.TodoSearchResponse;
import org.example.expert.domain.todo.entity.QTodo;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.user.entity.QUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

public class TodoRepositoryCustomImpl implements TodoRepositoryCustom{

	private final JPAQueryFactory queryFactory;

	public TodoRepositoryCustomImpl(EntityManager entityManager) {
		this.queryFactory = new JPAQueryFactory(entityManager);
	}

	// QueryDSL
	@Override
	public Optional<Todo> findByIdWithUser(Long todoId) {
		QTodo todo = QTodo.todo;
		QUser user = QUser.user;

		return Optional.ofNullable(
			queryFactory
				.select(todo)
				.from(todo)
				.leftJoin(todo.user, user).fetchJoin()
				.where(todo.id.eq(todoId))
				.fetchOne()
		);
	}

	// 10 검색기능
	@Override
	public Page<TodoSearchResponse> searchTodos(String title, String nickname, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
		QTodo todo = QTodo.todo;
		QManager manager = QManager.manager;
		QUser user = QUser.user;
		QComment comment = QComment.comment;

		List<TodoSearchResponse> results = queryFactory
			.select(Projections.bean(TodoSearchResponse.class,
				todo.title.as("title"),
				manager.countDistinct().as("managerCount"),
				comment.count().as("commentCount")
			))
			.from(todo)
			.leftJoin(todo.managers, manager)
			.leftJoin(manager.user, user)
			.leftJoin(todo.comments, comment)
			.where(
				containsTitle(title),
				containsNickname(user, nickname),
				isAfterStartDate(todo, startDate),
				isBeforeEndDate(todo, endDate)
			)
			.groupBy(todo.id)
			.orderBy(todo.modifiedAt.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		long total = queryFactory
			.select(todo.count())
			.from(todo)
			.where(
				containsTitle(title),
				containsNickname(user, nickname),
				isAfterStartDate(todo, startDate),
				isBeforeEndDate(todo, endDate)
			)
			.fetchOne();

		return new PageImpl<>(results, pageable, total);
	}


	private BooleanExpression containsTitle(String title) {
		return StringUtils.hasText(title) ? QTodo.todo.title.containsIgnoreCase(title) : null;
	}

	private BooleanExpression containsNickname(QUser user, String nickname) {
		return StringUtils.hasText(nickname) ? user.nickname.containsIgnoreCase(nickname) : null;
	}

	private BooleanExpression isAfterStartDate(QTodo todo, LocalDateTime startDate) {
		return startDate != null ? todo.modifiedAt.goe(startDate) : null;
	}

	private BooleanExpression isBeforeEndDate(QTodo todo, LocalDateTime endDate) {
		return endDate != null ? todo.modifiedAt.loe(endDate) : null;
	}
}
