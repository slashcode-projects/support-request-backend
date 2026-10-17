package com.slashcode.supportticket.service;

import java.util.List;

import com.slashcode.supportticket.dto.ticket.AssignTicketRequest;
import com.slashcode.supportticket.dto.ticket.CreateTicketRequest;
import com.slashcode.supportticket.dto.ticket.TicketResponse;
import com.slashcode.supportticket.dto.ticket.UpdateTicketStatusRequest;

public interface TicketService {

	TicketResponse createTicket(CreateTicketRequest request);

	List<TicketResponse> getAllTickets();

	TicketResponse getTicketById(Long id);

	List<TicketResponse> getMyTickets(Long userId);

	TicketResponse updateStatus(Long id, UpdateTicketStatusRequest request);

	TicketResponse assignTicket(Long id, AssignTicketRequest request);

	void cancelTicket(Long id);
	
}
