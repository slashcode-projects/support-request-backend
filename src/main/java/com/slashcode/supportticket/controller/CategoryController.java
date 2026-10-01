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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.slashcode.supportticket.dto.category.CategoryResponse;
import com.slashcode.supportticket.dto.category.CreateCategoryRequest;
import com.slashcode.supportticket.dto.category.UpdateCategoryRequest;
import com.slashcode.supportticket.dto.category.UpdateCategoryStatusRequest;
import com.slashcode.supportticket.response.ApiResponse;
import com.slashcode.supportticket.service.CategoryService;
import com.slashcode.supportticket.util.ResponseUtils;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/categories")
public class CategoryController {
	
	private final CategoryService categoryService;
	
	public CategoryController(CategoryService categoryService) {
		this.categoryService = categoryService;
	}

	@PostMapping
	public ResponseEntity<ApiResponse<CategoryResponse>> createCategory(@Valid @RequestBody CreateCategoryRequest request){
		CategoryResponse response = categoryService.createCategory(request);
		return ResponseUtils.getResponse(HttpStatus.CREATED, "Category created successfully", response);
	}
	
	@GetMapping
	public ResponseEntity<ApiResponse<List<CategoryResponse>>> getAllCategories(){
		
		List<CategoryResponse> response = categoryService.getAllCategories();
		return ResponseUtils.getResponse(HttpStatus.OK, "Category fetched successfully", response);
		
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<CategoryResponse>> getCategoryById(@PathVariable Long id){
		
		CategoryResponse response = categoryService.getCatgeoryById(id);
		return ResponseUtils.getResponse(HttpStatus.OK, "Category fetched successfully", response);
		
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse<CategoryResponse>> updateCategory(@PathVariable Long id, @Valid @RequestBody UpdateCategoryRequest request){
		
		CategoryResponse response = categoryService.updateCategoryById(id, request);
		return ResponseUtils.getResponse(HttpStatus.OK, "Category updated successfully", response);
	}
	
	@PatchMapping("/{id}/status")
	public ResponseEntity<ApiResponse<CategoryResponse>> updateCategoryStatus(@PathVariable Long id, @Valid @RequestBody UpdateCategoryStatusRequest request){
		
		CategoryResponse response = categoryService.updateCategoryStatusById(id, request);
		return ResponseUtils.getResponse(HttpStatus.OK, "Category status updated successfully", response);
	}

}
