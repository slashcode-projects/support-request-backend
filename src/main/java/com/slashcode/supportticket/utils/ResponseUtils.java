package com.slashcode.supportticket.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.slashcode.supportticket.response.ApiResponse;

public final class ResponseUtils {

	public static <T> ResponseEntity<ApiResponse<T>> getResponse(HttpStatus httpStatus, String message,
			T response) {
		
		return ResponseEntity
				.status(httpStatus)
				.body(
						new ApiResponse<>(
						httpStatus.value(),
						message,
						response
						)
				);
	}
	
	

}
