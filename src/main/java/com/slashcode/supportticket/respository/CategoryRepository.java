package com.slashcode.supportticket.respository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.slashcode.supportticket.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long>{
	
	boolean existsByNameIgnoreCase(String name);

}
