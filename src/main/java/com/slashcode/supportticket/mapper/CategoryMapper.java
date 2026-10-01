package com.slashcode.supportticket.mapper;

import org.springframework.stereotype.Component;

import com.slashcode.supportticket.dto.category.CategoryResponse;
import com.slashcode.supportticket.dto.category.CreateCategoryRequest;
import com.slashcode.supportticket.dto.category.UpdateCategoryRequest;
import com.slashcode.supportticket.dto.category.UpdateCategoryStatusRequest;
import com.slashcode.supportticket.model.Category;

@Component
public class CategoryMapper {

	public CategoryResponse toResponse(Category category) {
		CategoryResponse response = new CategoryResponse();
		response.setId(category.getId());
		response.setName(category.getName());
		response.setActive(category.getActive());
		return response;
	}
	
	public Category toEntity(CreateCategoryRequest request) {
		Category category = new Category();
		category.setName(request.getName());
		category.setActive(true);
		return category;
	}
	
	public void toEntity(Category category, UpdateCategoryRequest request) {
		category.setName(request.getName());
	}
	
	public void toEntity(Category category, UpdateCategoryStatusRequest request) {
		category.setActive(request.getStatus());
	}
	
}
