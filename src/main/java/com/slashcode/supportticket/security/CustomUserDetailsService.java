package com.slashcode.supportticket.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.slashcode.supportticket.exception.ResourceNotFoundException;
import com.slashcode.supportticket.model.User;
import com.slashcode.supportticket.respository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService{

	private final UserRepository userRepository;
	
	public CustomUserDetailsService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User user = userRepository.findByEmailIgnoreCase(username)
				.orElseThrow(() -> new ResourceNotFoundException("User not Found"));
		
		return new CustomUserDetails(user);
	}

}
