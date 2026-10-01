package com.slashcode.supportticket.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateCategoryRequest {

	@NotBlank(message = "Category name is required")
	@Size(max = 100, message = "Category name must not exceed 100 chars")
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
