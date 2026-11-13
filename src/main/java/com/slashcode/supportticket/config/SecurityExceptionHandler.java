package com.slashcode.supportticket.config;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.slashcode.supportticket.response.ApiResponse;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

@Component
public class SecurityExceptionHandler implements AuthenticationEntryPoint, AccessDeniedHandler{

	
	private final ObjectMapper objectMapper;
	
	public SecurityExceptionHandler(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}
	
	private void writeResponse(
			HttpServletResponse response,
			HttpStatus status,
			String message) throws JacksonException, IOException {
		
		response.setStatus(status.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		
		ApiResponse<Void> apiResponse = new ApiResponse<>(
				status.value(),
				message,
				null
				);
		objectMapper.writeValue(response.getOutputStream(), apiResponse);
	}

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		writeResponse(response, HttpStatus.FORBIDDEN, "You do not have permission to access this resource");
	}

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		writeResponse(response, HttpStatus.UNAUTHORIZED, "Authentication is required to access this resource");
	}

}
