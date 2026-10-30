package com.slashcode.supportticket.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.slashcode.supportticket.dto.ticket.AssignTicketRequest;
import com.slashcode.supportticket.dto.ticket.CreateTicketRequest;
import com.slashcode.supportticket.dto.ticket.TicketResponse;
import com.slashcode.supportticket.dto.ticket.UpdateTicketStatusRequest;
import com.slashcode.supportticket.enums.TicketStatus;
import com.slashcode.supportticket.enums.UserRole;
import com.slashcode.supportticket.exception.InvalidTicketAssignmentException;
import com.slashcode.supportticket.exception.InvalidTicketStatusException;
import com.slashcode.supportticket.exception.ResourceNotFoundException;
import com.slashcode.supportticket.mapper.TicketMapper;
import com.slashcode.supportticket.model.Category;
import com.slashcode.supportticket.model.Ticket;
import com.slashcode.supportticket.model.User;
import com.slashcode.supportticket.respository.CategoryRepository;
import com.slashcode.supportticket.respository.TicketRepository;
import com.slashcode.supportticket.respository.UserRepository;
import com.slashcode.supportticket.service.TicketService;

@Service
public class TicketServiceImpl implements TicketService{
	
	private final TicketRepository ticketRepository;
	private final UserRepository userRepository;
	private final CategoryRepository categoryRepository;
	private final TicketMapper ticketMapper;
	
	public TicketServiceImpl(TicketRepository ticketRepository, UserRepository userRepository,
			CategoryRepository categoryRepository, TicketMapper ticketMapper) {
		this.ticketRepository = ticketRepository;
		this.userRepository = userRepository;
		this.categoryRepository = categoryRepository;
		this.ticketMapper = ticketMapper;
	}
	
	private User findUserOrThrow(Long id) {
		return userRepository.findById(id)
        .orElseThrow(
                () -> new ResourceNotFoundException(
                        "User not found with id " + id
                )
        );
	}
	
	private Category findCategoryOrThrow(Long id) {
		return categoryRepository.findById(id)
		.orElseThrow(
				() -> new ResourceNotFoundException("Category not found with id : "+id)
				);
	}
	
	private Ticket findTicketOrThrow(Long id) {
		 return ticketRepository.findById(id)
			.orElseThrow(
					() -> new ResourceNotFoundException("Ticket not found with id: "+id));
	}

	@Override
	public TicketResponse createTicket(CreateTicketRequest request) {
		
		User user = findUserOrThrow(request.getCreatedById());
		Category category = findCategoryOrThrow(request.getCategoryId());
		
		if(!category.getActive()) {
			throw new IllegalArgumentException("Category is inactive");
		}
		
		Ticket ticket = ticketMapper.toEntity(request, category, user);
		ticket.setTicketNumber(generateTicketNumber());
		Ticket savedTicket = ticketRepository.save(ticket);
		return ticketMapper.toResponse(savedTicket);
	}

	@Override
	public List<TicketResponse> getAllTickets() {
		return ticketRepository.findAll()
			.stream()
			.map(ticketMapper::toResponse)
			.toList();			
	}	
	
	@Override
	public TicketResponse getTicketById(Long id) {
		Ticket ticket = findTicketOrThrow(id);
		return ticketMapper.toResponse(ticket);
	}
	
	
	@Override
	public List<TicketResponse> getMyTickets(Long userId) {
		findUserOrThrow(userId);
		return ticketRepository.findByCreatedById(userId)
				.stream()
				.map(ticketMapper::toResponse)
				.toList();
	}
	
	@Override
	public TicketResponse updateStatus(Long id, UpdateTicketStatusRequest request) {
		
		Ticket ticket = findTicketOrThrow(id);
		
		request.validateStatusTransition(ticket.getStatus());
		
		ticket.setStatus(request.getStatus());
		Ticket updatedTicket = ticketRepository.save(ticket);
		return ticketMapper.toResponse(updatedTicket);
	}
	

	@Override
	public TicketResponse assignTicket(Long id, AssignTicketRequest request) {
		Ticket ticket = findTicketOrThrow(id);
		
		User supportUser = findUserOrThrow(request.getSupportUserId());
		
		if(supportUser.getRole() != UserRole.SUPPORT) {
			throw new InvalidTicketAssignmentException("Ticket can only be assigned to a support user");
		}
		
		if(supportUser.getStatus().name().equals("INACTIVE")) {
			throw new InvalidTicketAssignmentException("Ticket cannot be assigned to inactive user");
		}
		
		if(ticket.getStatus() == TicketStatus.CLOSED || 
				ticket.getStatus() == TicketStatus.CANCELLED) {
			throw new InvalidTicketAssignmentException("Closed or cancelled ticket cannot be assigned");
		}
		
		ticket.setAssignedTo(supportUser);
		
		if(ticket.getStatus() == TicketStatus.OPEN) {
			ticket.setStatus(TicketStatus.ASSIGNED);
		}
		
		Ticket updatedTicket = ticketRepository.save(ticket);
		
		return ticketMapper.toResponse(updatedTicket);
	}
	
	@Override
	public void cancelTicket(Long id) {
		Ticket ticket = findTicketOrThrow(id);
		if(ticket.getStatus() == TicketStatus.CLOSED) {
			throw new InvalidTicketStatusException("Closed ticket cannot be cancelled");
		}
		if(ticket.getStatus() == TicketStatus.CANCELLED) {
			throw new InvalidTicketStatusException("Ticket is already cancelled");
		}
		ticket.setStatus(TicketStatus.CANCELLED);
		ticketRepository.save(ticket);
	}

	private String generateTicketNumber() {
		Long count = ticketRepository.count();
		return "INC-"+(1000001+count);
	}


}
