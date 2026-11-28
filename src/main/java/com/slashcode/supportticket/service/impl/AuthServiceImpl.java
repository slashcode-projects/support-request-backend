package com.slashcode.supportticket.service.impl;

import java.time.Instant;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.slashcode.supportticket.dto.auth.LoginRequest;
import com.slashcode.supportticket.dto.auth.TokenResponse;
import com.slashcode.supportticket.exception.ResourceNotFoundException;
import com.slashcode.supportticket.model.User;
import com.slashcode.supportticket.respository.UserRepository;
import com.slashcode.supportticket.security.CustomUserDetails;
import com.slashcode.supportticket.security.JwtService;
import com.slashcode.supportticket.service.AuthService;
import com.slashcode.supportticket.session.model.UserSession;
import com.slashcode.supportticket.session.repository.UserSessionRepository;

@Service
public class AuthServiceImpl implements AuthService{

	private final AuthenticationManager authenticationManager;
	private final JwtService jwtService;
	
	private final UserRepository userRepository;
	private final UserSessionRepository userSessionRepository;
	
	
	public AuthServiceImpl(AuthenticationManager authenticationManager, JwtService jwtService, UserSessionRepository userSessionRepository, UserRepository userRepository) {
		this.authenticationManager = authenticationManager;
		this.jwtService = jwtService;
		this.userSessionRepository = userSessionRepository;
		this.userRepository = userRepository;
	}

	@Override
	public TokenResponse login(LoginRequest request, String deviceInfo, String ipAddress) {
		
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
				);
		
		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		
		User user = userRepository.findById(userDetails.getId())
				.orElseThrow(() -> new ResourceNotFoundException("User not found"));
		
		Instant now = Instant.now();
		Instant sessionExpireAt = now.plusSeconds(jwtService.getAccessTokenTtlSeconds());
		UserSession session = new UserSession();
		session.setUser(user);
		session.setDeviceInfo(deviceInfo);
		session.setIpAddress(ipAddress);
		session.setCreatedAt(now);
		session.setLastUsedAt(now);
		session.setExpireAt(sessionExpireAt);
		
		UserSession savedSession = userSessionRepository.save(session);
		
		String accessToken = jwtService.generateAccessToken(userDetails, savedSession.getId());
		
		return new TokenResponse(accessToken, "Bearer", jwtService.getAccessTokenTtlSeconds());
		
	}

}
