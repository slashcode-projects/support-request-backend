package com.slashcode.supportticket.dto.comment;

import com.slashcode.supportticket.enums.CommentVisiblity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CreateCommentRequest {
	
	@NotBlank(message = "Comment message is required")
	@Size(max= 2000, message="Comment cannot exceed 2000 chars")
	private String message;
	
	@NotNull(message = "User Id is required")
	private Long userId;
	
	@NotNull(message="Comment Visibility is required")
	private CommentVisiblity visiblity;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public CommentVisiblity getVisiblity() {
		return visiblity;
	}

	public void setVisiblity(CommentVisiblity visiblity) {
		this.visiblity = visiblity;
	}


}
