package com.slashcode.supportticket.exception;

public class InvalidTicketStatusException extends RuntimeException{
	
	public InvalidTicketStatusException(String message) {
		super(message);
	}

}
