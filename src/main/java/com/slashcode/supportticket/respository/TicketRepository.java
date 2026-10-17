package com.slashcode.supportticket.respository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.slashcode.supportticket.model.Ticket;
import java.util.List;


public interface TicketRepository extends JpaRepository<Ticket, Long>{

	List<Ticket> findByCreatedById(Long userId);
}
