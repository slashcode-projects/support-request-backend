package com.slashcode.supportticket.mapper;

import org.springframework.stereotype.Component;

import com.slashcode.supportticket.dto.ticket.CreateTicketRequest;
import com.slashcode.supportticket.dto.ticket.TicketResponse;
import com.slashcode.supportticket.enums.TicketStatus;
import com.slashcode.supportticket.model.Category;
import com.slashcode.supportticket.model.Ticket;
import com.slashcode.supportticket.model.User;

@Component
public class TicketMapper {
	
	public TicketResponse toResponse(Ticket ticket) {
		TicketResponse response = new TicketResponse();
		
		response.setId(ticket.getId());
		response.setTicketNumber(ticket.getTicketNumber());
		response.setTitle(ticket.getTitle());
		response.setDescription(ticket.getDescription());
		response.setPriority(ticket.getPriority());
		response.setStatus(ticket.getStatus());
		
		response.setCategoryId(ticket.getCategory().getId());
		response.setCategoryName(ticket.getCategory().getName());
		
		response.setCreatedById(ticket.getCreatedBy().getId());
		response.setCreatedByName(ticket.getCreatedBy().getFirstName()+" "+ticket.getCreatedBy().getLastName());
		
		if(ticket.getAssignedTo()!=null) {
			response.setAssignedToId(ticket.getAssignedTo().getId());
			response.setAssignedToName(ticket.getAssignedTo().getFirstName()+" "+ticket.getAssignedTo().getLastName());
		}
		
		response.setCreatedAt(ticket.getCreatedAt());
		response.setUpdatedAt(ticket.getUpdatedAt());
		
		return response;
	}

	public Ticket toEntity(CreateTicketRequest request, Category category, User user) {
		Ticket ticket = new Ticket();
		
		ticket.setTitle(request.getTitle());
		ticket.setDescription(request.getDescription());
		ticket.setPriority(request.getPriority());
		ticket.setCategory(category);
		ticket.setCreatedBy(user);
		
		ticket.setStatus(TicketStatus.OPEN);
		return ticket;
	}
	
}
