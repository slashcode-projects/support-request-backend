package com.slashcode.supportticket.mapper;

import org.springframework.stereotype.Component;

import com.slashcode.supportticket.dto.comment.CommentResponse;
import com.slashcode.supportticket.dto.comment.CreateCommentRequest;
import com.slashcode.supportticket.model.Comment;
import com.slashcode.supportticket.model.Ticket;
import com.slashcode.supportticket.model.User;

@Component
public class CommentMapper {

	public CommentResponse toResponse(Comment savedComment) {
		CommentResponse response = new CommentResponse();
		response.setId(savedComment.getId());
		response.setTicketId(savedComment.getTicket().getId());
		response.setUserId(savedComment.getCommentedBy().getId());
		response.setUserName(savedComment.getCommentedBy().getFirstName()+ " "+savedComment.getCommentedBy().getLastName());
		response.setMessage(savedComment.getMessage());
		response.setVisiblity(savedComment.getCommentVisiblity());
		response.setCreatedAt(savedComment.getCreatedAt());
		return response;
	}

	
	public Comment toEntity(CreateCommentRequest request, Ticket ticket, User user) {
		Comment comment = new Comment();
		comment.setTicket(ticket);
		comment.setCommentedBy(user);
		comment.setMessage(request.getMessage().trim());
		comment.setCommentVisiblity(request.getVisiblity());
		return comment;
	}
}
