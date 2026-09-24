package com.slashcode.supportticket.model;

import java.time.LocalDateTime;

import com.slashcode.supportticket.enums.CommentVisiblity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name ="comments")
public class Comment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name ="ticket_id", nullable = false)
	private Ticket ticket;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User commented_by;

	@Column(nullable = false, length = 2000)
	private String message;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private CommentVisiblity commentVisiblity;
	
	private LocalDateTime createdAt;
	
	@PrePersist
	public void perPersist() {
		createdAt = LocalDateTime.now();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Ticket getTicket() {
		return ticket;
	}

	public void setTicket(Ticket ticket) {
		this.ticket = ticket;
	}

	public User getCommented_by() {
		return commented_by;
	}

	public void setCommented_by(User commented_by) {
		this.commented_by = commented_by;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public CommentVisiblity getCommentVisiblity() {
		return commentVisiblity;
	}

	public void setCommentVisiblity(CommentVisiblity commentVisiblity) {
		this.commentVisiblity = commentVisiblity;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	
	
	
	
}
