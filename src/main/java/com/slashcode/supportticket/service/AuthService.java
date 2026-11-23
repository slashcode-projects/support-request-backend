package com.slashcode.supportticket.service;

import com.slashcode.supportticket.dto.auth.LoginRequest;
import com.slashcode.supportticket.dto.auth.TokenResponse;

public interface AuthService {

	TokenResponse login(LoginRequest request);
	
}
