package com.slashcode.supportticket.dto.ticket;

import jakarta.validation.constraints.NotNull;

public class AssignTicketRequest {

	
	@NotNull(message = "Support user is required")
	private Long supportUserId;

	public Long getSupportUserId() {
		return supportUserId;
	}

	public void setSupportUserId(Long supportUserId) {
		this.supportUserId = supportUserId;
	}
	
	
	
	
	
}
