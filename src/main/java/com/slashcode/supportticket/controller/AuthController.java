package com.slashcode.supportticket.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.slashcode.supportticket.dto.auth.LoginRequest;
import com.slashcode.supportticket.dto.auth.TokenResponse;
import com.slashcode.supportticket.response.ApiResponse;
import com.slashcode.supportticket.service.AuthService;
import com.slashcode.supportticket.util.ResponseUtils;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

	private final AuthService authService;
	
	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@PostMapping("/login")
	public ResponseEntity<ApiResponse<TokenResponse>> login(@Valid @RequestBody LoginRequest request){
		
		TokenResponse response = authService.login(request);
		return ResponseUtils.getResponse(HttpStatus.OK, "Login Successful", response);
	}
	
}
