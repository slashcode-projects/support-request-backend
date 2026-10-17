package com.slashcode.supportticket.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.slashcode.supportticket.dto.ticket.AssignTicketRequest;
import com.slashcode.supportticket.dto.ticket.CreateTicketRequest;
import com.slashcode.supportticket.dto.ticket.TicketResponse;
import com.slashcode.supportticket.dto.ticket.UpdateTicketStatusRequest;
import com.slashcode.supportticket.response.ApiResponse;
import com.slashcode.supportticket.service.TicketService;
import com.slashcode.supportticket.util.ResponseUtils;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/tickets")
public class TicketController {

	private final TicketService ticketService;
	
	public TicketController(TicketService ticketService) {
		this.ticketService = ticketService;
	}

	@PostMapping
	public ResponseEntity<ApiResponse<TicketResponse>> createTicket(@Valid @RequestBody CreateTicketRequest request){
		TicketResponse response = ticketService.createTicket(request);
		return ResponseUtils.getResponse(HttpStatus.CREATED, "Ticket created successfully", response);
	}
	
	@GetMapping
	public ResponseEntity<ApiResponse<List<TicketResponse>>> getAllTickets(){
		List<TicketResponse> response = ticketService.getAllTickets();
		return ResponseUtils.getResponse(HttpStatus.OK, "Tickets fecthed successfully", response);
		
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<TicketResponse>> getTicketById(@PathVariable Long id){
		TicketResponse response = ticketService.getTicketById(id);
		return ResponseUtils.getResponse(HttpStatus.OK, "Ticket fecthed successfully", response);
		
	}
	
	@GetMapping("/my")
	public ResponseEntity<ApiResponse<List<TicketResponse>>> getMyTickets(@RequestParam Long userId){
		List<TicketResponse> response = ticketService.getMyTickets(userId);
		return ResponseUtils.getResponse(HttpStatus.OK, "Tickets fecthed successfully", response);
		
	}
	
	@PutMapping("/{id}/status")
	public ResponseEntity<ApiResponse<TicketResponse>> updateStatus(@PathVariable Long id, @Valid @RequestBody UpdateTicketStatusRequest request){
		
		TicketResponse response = ticketService.updateStatus(id, request);
		return ResponseUtils.getResponse(HttpStatus.OK, "Ticket updated successfully", response);
	}
	
	@PutMapping("/{id}/assign")
	public ResponseEntity<ApiResponse<TicketResponse>> assignTicket(@PathVariable Long id, @Valid @RequestBody AssignTicketRequest request){
		
		TicketResponse response = ticketService.assignTicket(id, request);
		return ResponseUtils.getResponse(HttpStatus.OK, "Ticket assigned successfully", response);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<Void>> cancelTicket(@PathVariable Long id){
		ticketService.cancelTicket(id);
		return ResponseUtils.getResponse(HttpStatus.OK, "Ticket cancelled successfully", null);
	}
}
