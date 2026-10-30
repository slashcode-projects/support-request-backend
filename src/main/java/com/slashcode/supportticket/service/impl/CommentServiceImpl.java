package com.slashcode.supportticket.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.slashcode.supportticket.dto.comment.CommentResponse;
import com.slashcode.supportticket.dto.comment.CreateCommentRequest;
import com.slashcode.supportticket.enums.CommentVisiblity;
import com.slashcode.supportticket.enums.UserRole;
import com.slashcode.supportticket.exception.InvalidCommentVisibilityException;
import com.slashcode.supportticket.exception.ResourceNotFoundException;
import com.slashcode.supportticket.mapper.CommentMapper;
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
	private final CommentMapper commentMapper;
	
	public CommentServiceImpl(TicketRepository ticketRepository, UserRepository userRepository,
			CommentRepository commentRepository, CommentMapper commentMapper) {
		this.ticketRepository = ticketRepository;
		this.userRepository = userRepository;
		this.commentRepository = commentRepository;
		this.commentMapper = commentMapper;
	}

	private Ticket findTicketOrThrow(Long id) {
		 return ticketRepository.findById(id)
			.orElseThrow(
					() -> new ResourceNotFoundException("Ticket not found with id: "+id));
	}
	
	private User findUserOrThrow(Long id) {
		return userRepository.findById(id)
        .orElseThrow(
                () -> new ResourceNotFoundException(
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
		
		Comment comment = commentMapper.toEntity(request, ticket, user);
		Comment savedComment = commentRepository.save(comment);
		return commentMapper.toResponse(savedComment);
	}
	
	@Override
	public List<CommentResponse> getCommentsByTicketId(Long ticketId) {
		findTicketOrThrow(ticketId);
		
		return commentRepository.findByTicketIdOrderByCreatedAtAsc(ticketId)
			.stream()
			.map(commentMapper::toResponse)
			.toList();
	}

	
	

}
