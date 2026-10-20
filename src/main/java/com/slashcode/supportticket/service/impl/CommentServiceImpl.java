package com.slashcode.supportticket.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.slashcode.supportticket.dto.comment.CommentResponse;
import com.slashcode.supportticket.dto.comment.CreateCommentRequest;
import com.slashcode.supportticket.enums.CommentVisiblity;
import com.slashcode.supportticket.enums.UserRole;
import com.slashcode.supportticket.exception.InvalidCommentVisibilityException;
import com.slashcode.supportticket.exception.TicketNotFoundException;
import com.slashcode.supportticket.exception.UserNotFoundException;
import com.slashcode.supportticket.model.Comment;
import com.slashcode.supportticket.model.Ticket;
import com.slashcode.supportticket.model.User;
import com.slashcode.supportticket.respository.CommentRepository;
import com.slashcode.supportticket.respository.TicketRepository;
import com.slashcode.supportticket.respository.UserRepository;
import com.slashcode.supportticket.service.CommentService;

@Service
public class CommentServiceImpl implements CommentService{
	
	private final TicketRepository ticketRepository;
	private final UserRepository userRepository;
	private final CommentRepository commentRepository;
	
	public CommentServiceImpl(TicketRepository ticketRepository, UserRepository userRepository,
			CommentRepository commentRepository) {
		this.ticketRepository = ticketRepository;
		this.userRepository = userRepository;
		this.commentRepository = commentRepository;
	}

	private Ticket findTicketOrThrow(Long id) {
		 return ticketRepository.findById(id)
			.orElseThrow(
					() -> new TicketNotFoundException("Ticket not found with id: "+id));
	}
	
	private User findUserOrThrow(Long id) {
		return userRepository.findById(id)
        .orElseThrow(
                () -> new UserNotFoundException(
                        "User not found with id " + id
                )
        );
	}

	@Override
	public CommentResponse addComment(Long ticketId, CreateCommentRequest request) {
		Ticket ticket = findTicketOrThrow(ticketId);
		User user = findUserOrThrow(request.getUserId());
		
		if(user.getRole() == UserRole.EMPLOYEE && request.getVisiblity() == CommentVisiblity.INTERNAL) {
			throw new InvalidCommentVisibilityException("Employee can only create public comments");
		}
		
		Comment comment = new Comment();
		comment.setTicket(ticket);
		comment.setCommentedBy(user);
		comment.setMessage(request.getMessage().trim());
		comment.setCommentVisiblity(request.getVisiblity());
		
		Comment savedComment = commentRepository.save(comment);
		return mapToResponse(savedComment);
	}
	
	@Override
	public List<CommentResponse> getCommentsByTicketId(Long ticketId) {
		findTicketOrThrow(ticketId);
		
		return commentRepository.findByTicketIdOrderByCreatedAtAsc(ticketId)
			.stream()
			.map(this::mapToResponse)
			.toList();
	}

	private CommentResponse mapToResponse(Comment savedComment) {
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

	

}
