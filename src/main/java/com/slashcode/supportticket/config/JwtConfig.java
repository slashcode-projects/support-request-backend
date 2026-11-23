package com.slashcode.supportticket.config;

import java.util.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import com.nimbusds.jose.jwk.source.ImmutableSecret;

@Configuration
@EnableConfigurationProperties(JwtProperties.class)
public class JwtConfig {

	@Bean
	public SecretKey jwtSecretKey(JwtProperties jwtProperties) {
		
		if(jwtProperties.getSecret() == null || jwtProperties.getSecret().isBlank()) {
			throw new IllegalStateException("JWT secret must be configured");
		}
		
		byte[] decodedKey;
		
		try {
			decodedKey = Base64.getDecoder().decode(jwtProperties.getSecret());
		}
		catch (IllegalArgumentException e) {
			throw new IllegalStateException("JWT secret key must be valid Base64 value", e);
		}
		
		if(decodedKey.length <32) {
			throw new IllegalStateException("JWT secret key mush contain 32 bytes");
		}
		
		return new SecretKeySpec(decodedKey, "HmacSHA256");
	}
	
	@Bean
	public JwtEncoder jwtEncoder(SecretKey secretKey) {
		return new NimbusJwtEncoder(
				new ImmutableSecret<>(secretKey)
				);
	}
	
	@Bean
	public JwtDecoder jwtDecoder(SecretKey jwtSecretKey, JwtProperties jwtProperties) {
		
		NimbusJwtDecoder decoder = NimbusJwtDecoder
				.withSecretKey(jwtSecretKey)
				.macAlgorithm(MacAlgorithm.HS256)
				.build();
		
		decoder.setJwtValidator(
				JwtValidators.createDefaultWithIssuer(jwtProperties.getIssuer())
				);
		
		return decoder;
	}
	
	
	@Bean
	public JwtAuthenticationConverter jwtAuthenticationConverter() {
		
		JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();
		
		authoritiesConverter.setAuthoritiesClaimName("roles");
		authoritiesConverter.setAuthorityPrefix("");
		
		JwtAuthenticationConverter authenticationConverter = new JwtAuthenticationConverter();
		authenticationConverter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);
		
		return authenticationConverter;
	}

	
}
