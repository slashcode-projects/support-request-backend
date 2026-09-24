package com.slashcode.supportticket.mapper;

import org.springframework.stereotype.Component;

import com.slashcode.supportticket.dto.user.CreateUserRequest;
import com.slashcode.supportticket.dto.user.UpdateUserRequest;
import com.slashcode.supportticket.dto.user.UpdateUserStatusRequest;
import com.slashcode.supportticket.dto.user.UserResponse;
import com.slashcode.supportticket.enums.UserStatus;
import com.slashcode.supportticket.model.User;

@Component
public class UserMapper {

	public UserResponse toReponse(User user) {
		UserResponse response = new UserResponse();
		response.setId(user.getId());
		response.setFirstName(user.getFirstName());
		response.setLastName(user.getLastName());
		response.setEmail(user.getEmail());
		response.setRole(user.getRole());
		response.setStatus(user.getStatus());
		response.setDepartment(user.getDepartment());
		response.setCreatedAt(user.getCreatedAt());
		response.setUpdatedAt(user.getUpdatedAt());
		return response;
	}

	/** used in create user */
	public User toEntity(CreateUserRequest request) {
		User user = new User();
		user.setFirstName(request.getFirstName());
		user.setLastName(request.getLastName());
		user.setEmail(request.getEmail());
		user.setPassword(request.getPassword());
		user.setDepartment(request.getDepartment());
		user.setRole(request.getRole());
		user.setStatus(UserStatus.ACTIVE);
		return user;
	}

	/** used in update user */
	public void toEntity(User user, UpdateUserRequest request) {
		user.setFirstName(request.getFirstName());
		user.setLastName(request.getLastName());
		user.setEmail(request.getEmail());
		user.setRole(request.getRole());
		user.setDepartment(request.getDepartment());		
	}

	/** used in update user status */
	public void toEntity(User user, UpdateUserStatusRequest request) {
		user.setStatus(request.getStatus());
	}
	
}
