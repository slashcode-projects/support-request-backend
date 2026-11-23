package com.slashcode.supportticket.security;

import java.time.Instant;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import com.slashcode.supportticket.config.JwtProperties;

@Service
public class JwtService {

	private final JwtEncoder jwtEncoder;
	private final JwtProperties jwtProperties;
	
	public JwtService(JwtEncoder jwtEncoder, JwtProperties jwtProperties) {
		
		this.jwtEncoder = jwtEncoder;
		this.jwtProperties = jwtProperties;
	}
	
	public String generateAccessToken(CustomUserDetails userDetails) {
		
		Instant issuedAt = Instant.now();
		
		List<String> roles = userDetails.getAuthorities()
							.stream()
							.map(GrantedAuthority::getAuthority)
							.toList();
		
		JwtClaimsSet claims = JwtClaimsSet.builder()
							.issuer(jwtProperties.getIssuer())
							.issuedAt(issuedAt)
							.expiresAt(issuedAt.plusSeconds(jwtProperties.getAccessTokenTtlSeconds()))
							.subject(userDetails.getUsername())
							.claim("userId", userDetails.getId())
							.claim("roles", roles)
							.build();
		
		JwsHeader header = JwsHeader
						.with(MacAlgorithm.HS256)
						.type("JWT")
						.build();
		
		return jwtEncoder.encode(
				JwtEncoderParameters.from(header, claims)
				).getTokenValue();
		
	}

	public long getAccessTokenTtlSeconds() {
		return jwtProperties.getAccessTokenTtlSeconds();
	}
	
	/****
	 * 
	 {
	 	"iss" : "support-ticket-api",
	 	"sub" : "super.admin@gmail.com",
	 	"userId" : 10,
	 	"roles" : [
	 		"ROLE_ADMIN"
	 	],
	 	"iat" : 1793480000
	 	"exp : 1793480900
	 }
	 
	 * 
	 */
	
}
