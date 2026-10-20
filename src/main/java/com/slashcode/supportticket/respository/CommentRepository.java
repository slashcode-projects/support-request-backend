package com.slashcode.supportticket.respository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.slashcode.supportticket.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long>{

	List<Comment> findByTicketIdOrderByCreatedAtAsc(Long ticketId);
}
