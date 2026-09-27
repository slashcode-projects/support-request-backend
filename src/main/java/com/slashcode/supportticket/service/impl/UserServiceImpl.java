package com.slashcode.supportticket.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.slashcode.supportticket.dto.user.CreateUserRequest;
import com.slashcode.supportticket.dto.user.UpdateUserRequest;
import com.slashcode.supportticket.dto.user.UpdateUserStatusRequest;
import com.slashcode.supportticket.dto.user.UserResponse;
import com.slashcode.supportticket.exception.UserAlreadyExistException;
import com.slashcode.supportticket.exception.UserNotFoundException;
import com.slashcode.supportticket.mapper.UserMapper;
import com.slashcode.supportticket.model.User;
import com.slashcode.supportticket.respository.UserRepository;
import com.slashcode.supportticket.service.UserService;

@Service
public class UserServiceImpl implements UserService{

	private final UserRepository userRepository;
	private final UserMapper userMapper;
	
	public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
		this.userRepository = userRepository;
		this.userMapper = userMapper;
	}
	
	private User findUserOrThrow(Long id) {
		return userRepository.findById(id)
        .orElseThrow(
                () -> new UserNotFoundException(
                        "User not found with id " + id
                )
        );
	}
	

	@Override
	public UserResponse createUser(CreateUserRequest request) {
		
		if(userRepository.existsByEmail(request.getEmail())) {
			throw new UserAlreadyExistException("User is already exist with this email "+request.getEmail());
		}
		
		User user = userMapper.toEntity(request);
		User savedUser = userRepository.save(user);
		return userMapper.toResponse(savedUser);
	}


	@Override
	public List<UserResponse> getAllUsers() {
		return userRepository.findAll()
			.stream()
			.map(user -> userMapper.toResponse(user))
			.toList();
	}
	
	@Override
	public UserResponse getUserById(Long id) {
		User user = findUserOrThrow(id);
		return userMapper.toResponse(user);
	}
	
	@Override
	public UserResponse updateUser(Long id, UpdateUserRequest request) {
		
		User user = findUserOrThrow(id);
		
		userMapper.toEntity(user, request);
		
		User updatedUser = userRepository.save(user);
		return userMapper.toResponse(updatedUser);
	}
	
	@Override
	public UserResponse updateUserStatus(Long id, UpdateUserStatusRequest request) {
		
		User user = findUserOrThrow(id);
		userMapper.toEntity(user, request);
		
		User updatedUser = userRepository.save(user);
		return userMapper.toResponse(updatedUser);
	}
	
}
