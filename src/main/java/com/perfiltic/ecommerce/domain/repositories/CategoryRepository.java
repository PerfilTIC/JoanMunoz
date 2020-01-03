package com.perfiltic.ecommerce.domain.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.perfiltic.ecommerce.domain.model.Category;

public interface CategoryRepository {
	
	public Category getCategoryById(Long idCategory);
	
	public Page<Category> getCategoriesByCategory(Long idCategory, Pageable pageable);
	
	public Category saveCategory(Category category);
	
	public void deleteCategory(Long idCategory);

	public int countSubCategories(Long idCategory);
}
