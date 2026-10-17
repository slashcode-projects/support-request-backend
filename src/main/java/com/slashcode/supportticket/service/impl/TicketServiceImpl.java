package com.slashcode.supportticket.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.slashcode.supportticket.dto.ticket.AssignTicketRequest;
import com.slashcode.supportticket.dto.ticket.CreateTicketRequest;
import com.slashcode.supportticket.dto.ticket.TicketResponse;
import com.slashcode.supportticket.dto.ticket.UpdateTicketStatusRequest;
import com.slashcode.supportticket.enums.TicketStatus;
import com.slashcode.supportticket.enums.UserRole;
import com.slashcode.supportticket.exception.CategoryNotFoundException;
import com.slashcode.supportticket.exception.InvalidTicketAssignmentException;
import com.slashcode.supportticket.exception.InvalidTicketStatusException;
import com.slashcode.supportticket.exception.TicketNotFoundException;
import com.slashcode.supportticket.exception.UserNotFoundException;
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
	
	public TicketServiceImpl(TicketRepository ticketRepository, UserRepository userRepository,
			CategoryRepository categoryRepository) {
		this.ticketRepository = ticketRepository;
		this.userRepository = userRepository;
		this.categoryRepository = categoryRepository;
	}
	
	private User findUserOrThrow(Long id) {
		return userRepository.findById(id)
        .orElseThrow(
                () -> new UserNotFoundException(
                        "User not found with id " + id
                )
        );
	}
	
	private Category findCategoryOrThrow(Long id) {
		return categoryRepository.findById(id)
		.orElseThrow(
				() -> new CategoryNotFoundException("Category not found with id : "+id)
				);
	}
	
	private Ticket findTicketOrThrow(Long id) {
		 return ticketRepository.findById(id)
			.orElseThrow(
					() -> new TicketNotFoundException("Ticket not found with id: "+id));
	}

	@Override
	public TicketResponse createTicket(CreateTicketRequest request) {
		
		User user = findUserOrThrow(request.getCreatedById());
		
		Category category = findCategoryOrThrow(request.getCategoryId());
		
		if(!category.getActive()) {
			throw new IllegalArgumentException("Category is inactive");
		}
		
		Ticket ticket = new Ticket();
		
		ticket.setTitle(request.getTitle());
		ticket.setDescription(request.getDescription());
		ticket.setPriority(request.getPriority());
		ticket.setCategory(category);
		ticket.setCreatedBy(user);
		
		ticket.setStatus(TicketStatus.OPEN);
		ticket.setTicketNumber(generateTicketNumber());
		
		Ticket savedTicket = ticketRepository.save(ticket);
	
		return mapToResponse(savedTicket);
	}

	@Override
	public List<TicketResponse> getAllTickets() {
		return ticketRepository.findAll()
			.stream()
			.map(this::mapToResponse)
			.toList();			
	}	
	
	@Override
	public TicketResponse getTicketById(Long id) {
		Ticket ticket = findTicketOrThrow(id);
		return mapToResponse(ticket);
	}
	
	
	@Override
	public List<TicketResponse> getMyTickets(Long userId) {
		findUserOrThrow(userId);
		
		return ticketRepository.findByCreatedById(userId)
				.stream()
				.map(this::mapToResponse)
				.toList();
	}
	
	@Override
	public TicketResponse updateStatus(Long id, UpdateTicketStatusRequest request) {
		
		Ticket ticket = findTicketOrThrow(id);
		
		validateStatusTransition(ticket.getStatus(), request.getStatus());
		
		ticket.setStatus(request.getStatus());
		
		Ticket updatedTicket = ticketRepository.save(ticket);
		
		return mapToResponse(updatedTicket);
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
		
		return mapToResponse(updatedTicket);
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
	
	
	private void validateStatusTransition(TicketStatus current, TicketStatus next) {
		
		if(current == TicketStatus.CLOSED) {
			throw new InvalidTicketStatusException("Closed ticket cannot be updated");
		}
		
		if(current == TicketStatus.CANCELLED) {
			throw new InvalidTicketStatusException("Cancelled ticket cannot be updated");			
		}
		
		if(current == TicketStatus.OPEN
				&& next != TicketStatus.ASSIGNED 
				&& next != TicketStatus.CANCELLED
				) {
			throw new InvalidTicketStatusException("OPEN ticket can only be assigned or cancelled");
		}
		
		if(current == TicketStatus.ASSIGNED
				&& next != TicketStatus.CANCELLED
				&& next != TicketStatus.IN_PROGRESS
				) {
			throw new InvalidTicketStatusException("ASSIGNED ticket can only move to IN_PROGRESS or CANCELLED");
		}
		
		if(current == TicketStatus.IN_PROGRESS
				&& next != TicketStatus.WAITING_FOR_USER
				&& next != TicketStatus.RESOLVED
				) {
			throw new InvalidTicketStatusException("IN_PROGRESS ticket can only move to WAITING_FOR_USER or RESOLVED");
		}
		
		if(current == TicketStatus.WAITING_FOR_USER 
				&& next != TicketStatus.IN_PROGRESS) {
			throw new InvalidTicketStatusException("WAITING_FOR_USER ticket can only move to IN_PROGRESS");
		}
		
		
		if(current == TicketStatus.RESOLVED
				&& next != TicketStatus.CLOSED
				&& next != TicketStatus.IN_PROGRESS) {
			throw new InvalidTicketStatusException("RESOLVED ticket can only be closed ot reopned");
		}
		
	}

	private TicketResponse mapToResponse(Ticket ticket) {
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

	private String generateTicketNumber() {
		Long count = ticketRepository.count();
		return "INC-"+(1000001+count);
	}


}
