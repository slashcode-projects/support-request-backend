package com.slashcode.supportticket.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.slashcode.supportticket.dto.comment.CommentResponse;
import com.slashcode.supportticket.dto.comment.CreateCommentRequest;
import com.slashcode.supportticket.response.ApiResponse;
import com.slashcode.supportticket.service.CommentService;
import com.slashcode.supportticket.util.ResponseUtils;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/tickets/{ticketId}/comments")
public class CommentController {

	private final CommentService commentService;
	
	public CommentController(CommentService commentService) {
		this.commentService = commentService;
	}

	@PostMapping
	public ResponseEntity<ApiResponse<CommentResponse>> addComment(
			@PathVariable Long ticketId,
			@Valid @RequestBody CreateCommentRequest request
			){
		
		CommentResponse commentResponse = commentService.addComment(ticketId, request);
		return ResponseUtils.getResponse(HttpStatus.CREATED, "Comment created successfully", commentResponse);
	}
	
	
	@GetMapping
	public ResponseEntity<ApiResponse<List<CommentResponse>>> getCommentsByTicketId(@PathVariable Long ticketId){
		List<CommentResponse> response = commentService.getCommentsByTicketId(ticketId);
		return ResponseUtils.getResponse(HttpStatus.OK, "Comments fetched successfully", response);
	}
	
	
}
