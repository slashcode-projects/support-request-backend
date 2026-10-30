package com.slashcode.supportticket.service;

import java.util.List;

import com.slashcode.supportticket.dto.category.CategoryResponse;
import com.slashcode.supportticket.dto.category.CreateCategoryRequest;
import com.slashcode.supportticket.dto.category.UpdateCategoryRequest;
import com.slashcode.supportticket.dto.category.UpdateCategoryStatusRequest;

public interface CategoryService {

	CategoryResponse createCategory(CreateCategoryRequest request);

	List<CategoryResponse> getAllCategories();

	CategoryResponse getCatgeoryById(Long id);

	CategoryResponse updateCategoryById(Long id, UpdateCategoryRequest request);

	CategoryResponse updateCategoryStatusById(Long id, UpdateCategoryStatusRequest request);

}
