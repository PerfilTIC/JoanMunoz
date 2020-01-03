package com.perfiltic.ecommerce.infrastructure.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.perfiltic.ecommerce.domain.model.Category;
import com.perfiltic.ecommerce.domain.repositories.CategoryRepository;
import com.perfiltic.ecommerce.infrastructure.daos.CategoryRepositoryDao;

public class CategoryRepositoryImp implements CategoryRepository {

	@Autowired
	private CategoryRepositoryDao categoryRepositoryDao;

	@Override
	public Page<Category> getCategoriesByCategory(Long idCategory, Pageable pageable) {
		return categoryRepositoryDao.findBySuperCategory(idCategory, pageable);
	}

	@Override
	public Category getCategoryById(Long idCategory) {
		return categoryRepositoryDao.findById(idCategory).orElse(null);
	}

	@Override
	public Category saveCategory(Category category) {
		return categoryRepositoryDao.save(category);
	}

	@Override
	public void deleteCategory(Long idCategory) {
		categoryRepositoryDao.deleteById(idCategory);
	}

	@Override
	public int countSubCategories(Long idCategory) {
		return categoryRepositoryDao.countBySuperCategory(idCategory);
	}
	
}
