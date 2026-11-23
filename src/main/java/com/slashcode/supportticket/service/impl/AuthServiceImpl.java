package com.slashcode.supportticket.service.impl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.slashcode.supportticket.dto.auth.LoginRequest;
import com.slashcode.supportticket.dto.auth.TokenResponse;
import com.slashcode.supportticket.security.CustomUserDetails;
import com.slashcode.supportticket.security.JwtService;
import com.slashcode.supportticket.service.AuthService;

@Service
public class AuthServiceImpl implements AuthService{

	private final AuthenticationManager authenticationManager;
	private final JwtService jwtService;
	
	public AuthServiceImpl(AuthenticationManager authenticationManager, JwtService jwtService) {
		this.authenticationManager = authenticationManager;
		this.jwtService = jwtService;
	}

	@Override
	public TokenResponse login(LoginRequest request) {
		
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
				);
		
		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		
		String accessToken = jwtService.generateAccessToken(userDetails);
		
		return new TokenResponse(accessToken, "Bearer", jwtService.getAccessTokenTtlSeconds());
		
	}

}
