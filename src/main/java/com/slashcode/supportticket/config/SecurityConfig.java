package com.slashcode.supportticket.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

	private final SecurityExceptionHandler securityExceptionHandler;
	
	public SecurityConfig(SecurityExceptionHandler securityExceptionHandler) {
		this.securityExceptionHandler = securityExceptionHandler;
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
					.requestMatchers("/auth/**").permitAll()
					.anyRequest().authenticated());
		
		return http.build();
	}
	
}
