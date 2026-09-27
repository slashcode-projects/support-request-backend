package com.slashcode.supportticket.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.slashcode.supportticket.dto.user.CreateUserRequest;
import com.slashcode.supportticket.dto.user.UpdateUserRequest;
import com.slashcode.supportticket.dto.user.UpdateUserStatusRequest;
import com.slashcode.supportticket.dto.user.UserResponse;
import com.slashcode.supportticket.response.ApiResponse;
import com.slashcode.supportticket.service.UserService;
import com.slashcode.supportticket.util.ResponseUtils;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {
	
	private final UserService userService;
	
	public UserController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping
	public ResponseEntity<ApiResponse<UserResponse>> createUser(@Valid @RequestBody CreateUserRequest request){
		return ResponseUtils.getResponse(HttpStatus.CREATED, "Users created successfully", userService.createUser(request));
	}
	
	@GetMapping
	public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers(){
		return ResponseUtils.getResponse(HttpStatus.OK, "Users fetched successfully", userService.getAllUsers());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable Long id){
		return ResponseUtils.getResponse(HttpStatus.OK, "Users fetched successfully",userService.getUserById(id));		
	}

	
	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse<UserResponse>> updateUser(@Valid @RequestBody UpdateUserRequest request, @PathVariable Long id){
		return ResponseUtils.getResponse(HttpStatus.OK, "User updated successfully", userService.updateUser(id,request));		
	}
	
	@PatchMapping("/{id}/status")
	public ResponseEntity<ApiResponse<UserResponse>> updateUserStatus(@Valid @RequestBody UpdateUserStatusRequest request, @PathVariable Long id){
		return ResponseUtils.getResponse(HttpStatus.OK, "User status updated successfully", userService.updateUserStatus(id,request));
	}
	
}
