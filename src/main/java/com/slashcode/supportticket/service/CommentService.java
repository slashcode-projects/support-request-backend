package com.slashcode.supportticket.service;

import java.util.List;

import com.slashcode.supportticket.dto.comment.CommentResponse;
import com.slashcode.supportticket.dto.comment.CreateCommentRequest;

public interface CommentService {

	CommentResponse addComment(Long ticketId, CreateCommentRequest request);

	List<CommentResponse> getCommentsByTicketId(Long ticketId);

}
