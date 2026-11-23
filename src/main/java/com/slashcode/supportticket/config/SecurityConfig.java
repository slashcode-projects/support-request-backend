package com.slashcode.supportticket.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

	private final SecurityExceptionHandler securityExceptionHandler;
	private final JwtAuthenticationConverter jwtAuthenticationConverter;
	
	public SecurityConfig(SecurityExceptionHandler securityExceptionHandler, JwtAuthenticationConverter jwtAuthenticationConverter) {
		this.securityExceptionHandler = securityExceptionHandler;
		this.jwtAuthenticationConverter = jwtAuthenticationConverter;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) {
		
		http
			.csrf(AbstractHttpConfigurer::disable)
			.httpBasic(AbstractHttpConfigurer::disable)
			.formLogin(AbstractHttpConfigurer::disable)
			
			.sessionManagement(session ->
					session.sessionCreationPolicy(
							SessionCreationPolicy.STATELESS))
			
			.exceptionHandling(exception -> exception
					.authenticationEntryPoint(securityExceptionHandler)
					.accessDeniedHandler(securityExceptionHandler))
			
			.authorizeHttpRequests(auth -> auth
					.requestMatchers("/auth/login").permitAll()
					.anyRequest().authenticated())
			
			.oauth2ResourceServer(oauth2 ->
					oauth2
					.jwt( jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter))
					.authenticationEntryPoint(securityExceptionHandler)
					.accessDeniedHandler(securityExceptionHandler)
					)
			;
		
		return http.build();
	}
	
}
