package com.slashcode.supportticket.exception;

public class TicketNotFoundException extends RuntimeException{
	
	public TicketNotFoundException(String message) {
		super(message);
	}

}
