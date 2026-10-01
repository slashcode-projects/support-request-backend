package com.slashcode.supportticket.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.slashcode.supportticket.dto.category.CategoryResponse;
import com.slashcode.supportticket.dto.category.CreateCategoryRequest;
import com.slashcode.supportticket.dto.category.UpdateCategoryRequest;
import com.slashcode.supportticket.dto.category.UpdateCategoryStatusRequest;
import com.slashcode.supportticket.exception.CategoryAlreadyExistsException;
import com.slashcode.supportticket.exception.CategoryNotFoundException;
import com.slashcode.supportticket.mapper.CategoryMapper;
import com.slashcode.supportticket.model.Category;
import com.slashcode.supportticket.respository.CategoryRepository;
import com.slashcode.supportticket.service.CategoryService;

import jakarta.validation.Valid;

@Service
public class CategoryServiceImpl implements CategoryService{

	private final CategoryRepository categoryRepository;
	private final CategoryMapper categoryMapper;
	
	public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
		this.categoryRepository = categoryRepository;
		this.categoryMapper = categoryMapper;
	}
	
	private Category findCategoryOrThrow(Long id) {
		return categoryRepository.findById(id)
		.orElseThrow(
				() -> new CategoryNotFoundException("Category not found with id : "+id)
				);
	}

	@Override
	public CategoryResponse createCategory(CreateCategoryRequest request) {
	
		if(categoryRepository.existsByNameIgnoreCase(request.getName())) {
			throw new CategoryAlreadyExistsException("Category already exists with name "+ request.getName());
		}
		
		Category category = categoryMapper.toEntity(request);
		Category savedCategory = categoryRepository.save(category);
		
		return categoryMapper.toResponse(savedCategory);
	}
	
	@Override
	public List<CategoryResponse> getAllCategories() {
		return 
		categoryRepository.findAll()
			.stream()
			.map(category -> categoryMapper.toResponse(category))
			.toList();
		
	}

	@Override
	public CategoryResponse getCatgeoryById(Long id) {
		
		Category category = findCategoryOrThrow(id);
		return categoryMapper.toResponse(category);
	}
	
	@Override
	public CategoryResponse updateCategoryById(Long id, @Valid UpdateCategoryRequest request) {
		Category category = findCategoryOrThrow(id);
		
		if(!category.getName().equals(request.getName()) && categoryRepository.existsByNameIgnoreCase(request.getName())) {
			throw new CategoryAlreadyExistsException("Category already exists with name "+ request.getName());
		}
		
		categoryMapper.toEntity(category, request);
		
		Category updatedCategory = categoryRepository.save(category);
		return categoryMapper.toResponse(updatedCategory);
	}


	@Override
	public CategoryResponse updateCategoryStatusById(Long id, @Valid UpdateCategoryStatusRequest request) {
		Category category = findCategoryOrThrow(id);
		
		categoryMapper.toEntity(category, request);
		
		Category updatedCategory = categoryRepository.save(category);
		return categoryMapper.toResponse(updatedCategory);
	}



}
