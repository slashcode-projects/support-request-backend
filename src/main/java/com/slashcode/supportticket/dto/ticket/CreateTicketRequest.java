package com.slashcode.supportticket.dto.ticket;

import com.slashcode.supportticket.enums.TicketPriority;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CreateTicketRequest {
	
	@NotBlank(message = "Title is required")
	@Size(max= 200, message = "Title cannot exceed 200 chars")
	private String title;
	
	@NotBlank(message = "Description is required")
	@Size(max= 2000, message = "Description cannot exceed 2000 chars")
	private String description;
	
	@NotNull(message = "Priority is required")
	private TicketPriority priority;
	
	@NotNull(message = "Category is required")
	private Long categoryId;
	
	@NotNull(message = "Created By User is required")
	private Long createdById;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public TicketPriority getPriority() {
		return priority;
	}

	public void setPriority(TicketPriority priority) {
		this.priority = priority;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Long getCreatedById() {
		return createdById;
	}

	public void setCreatedById(Long createdById) {
		this.createdById = createdById;
	}

}
