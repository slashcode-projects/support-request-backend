package com.slashcode.supportticket.dto.ticket;

import com.slashcode.supportticket.enums.TicketStatus;
import com.slashcode.supportticket.exception.InvalidTicketStatusException;

import jakarta.validation.constraints.NotNull;

public class UpdateTicketStatusRequest {

	@NotNull(message = "Status is required")
	private TicketStatus status;

	public TicketStatus getStatus() {
		return status;
	}

	public void setStatus(TicketStatus status) {
		this.status = status;
	}

	//current status represent db status
	public void validateStatusTransition(TicketStatus current) {
		if(current == TicketStatus.CLOSED) {
			throw new InvalidTicketStatusException("Closed ticket cannot be updated");
		}
		
		if(current == TicketStatus.CANCELLED) {
			throw new InvalidTicketStatusException("Cancelled ticket cannot be updated");			
		}
		
		if(current == TicketStatus.OPEN
				&& this.status != TicketStatus.ASSIGNED 
				&& this.status != TicketStatus.CANCELLED
				) {
			throw new InvalidTicketStatusException("OPEN ticket can only be assigned or cancelled");
		}
		
		if(current == TicketStatus.ASSIGNED
				&& this.status != TicketStatus.CANCELLED
				&& this.status != TicketStatus.IN_PROGRESS
				) {
			throw new InvalidTicketStatusException("ASSIGNED ticket can only move to IN_PROGRESS or CANCELLED");
		}
		
		if(current == TicketStatus.IN_PROGRESS
				&& this.status != TicketStatus.WAITING_FOR_USER
				&& this.status != TicketStatus.RESOLVED
				) {
			throw new InvalidTicketStatusException("IN_PROGRESS ticket can only move to WAITING_FOR_USER or RESOLVED");
		}
		
		if(current == TicketStatus.WAITING_FOR_USER 
				&& this.status != TicketStatus.IN_PROGRESS) {
			throw new InvalidTicketStatusException("WAITING_FOR_USER ticket can only move to IN_PROGRESS");
		}
		
		
		if(current == TicketStatus.RESOLVED
				&& this.status != TicketStatus.CLOSED
				&& this.status != TicketStatus.IN_PROGRESS) {
			throw new InvalidTicketStatusException("RESOLVED ticket can only be closed ot reopned");
		}
		
	}
	
	
}
