package com.slashcode.supportticket.service;

import java.util.List;

import com.slashcode.supportticket.dto.category.CategoryResponse;
import com.slashcode.supportticket.dto.category.CreateCategoryRequest;
import com.slashcode.supportticket.dto.category.UpdateCategoryRequest;
import com.slashcode.supportticket.dto.category.UpdateCategoryStatusRequest;

import jakarta.validation.Valid;

public interface CategoryService {

	CategoryResponse createCategory(CreateCategoryRequest request);

	List<CategoryResponse> getAllCategories();

	CategoryResponse getCatgeoryById(Long id);

	CategoryResponse updateCategoryById(Long id, @Valid UpdateCategoryRequest request);

	CategoryResponse updateCategoryStatusById(Long id, @Valid UpdateCategoryStatusRequest request);

}
