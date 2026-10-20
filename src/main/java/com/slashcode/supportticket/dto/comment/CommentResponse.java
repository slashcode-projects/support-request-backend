package com.slashcode.supportticket.dto.comment;

import java.time.LocalDateTime;

import com.slashcode.supportticket.enums.CommentVisiblity;

public class CommentResponse {
	
	private Long id;
	private Long ticketId;
	
	private Long userId;
	private String userName;
	private String message;
	private CommentVisiblity visiblity;
	private LocalDateTime createdAt;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getTicketId() {
		return ticketId;
	}
	public void setTicketId(Long ticketId) {
		this.ticketId = ticketId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public CommentVisiblity getVisiblity() {
		return visiblity;
	}
	public void setVisiblity(CommentVisiblity visiblity) {
		this.visiblity = visiblity;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	
	
	

}
