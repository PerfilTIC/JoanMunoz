package com.perfiltic.ecommerce.application.implement;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.perfiltic.ecommerce.application.services.categories.CreateCategoryService;
import com.perfiltic.ecommerce.application.services.categories.DeleteCategoryService;
import com.perfiltic.ecommerce.application.services.categories.GetCategoryService;
import com.perfiltic.ecommerce.application.services.categories.GetSubCategoriesService;
import com.perfiltic.ecommerce.application.services.categories.GetSuperCategoriesService;
import com.perfiltic.ecommerce.domain.exceptions.CategoryNotRemovableException;
import com.perfiltic.ecommerce.domain.model.Category;
import com.perfiltic.ecommerce.domain.repositories.CategoryRepository;
import com.perfiltic.ecommerce.domain.repositories.ProductRepository;

@Service
public class EcommerceService implements GetCategoryService, CreateCategoryService, DeleteCategoryService,
		GetSubCategoriesService, GetSuperCategoriesService {

	public static final String CATEGORY_NO_REMOVABLE = "The category is not removable because it has some products or subcategories.";
	
	private CategoryRepository categoryRepository;
	private ProductRepository productRepository;

	public EcommerceService(ProductRepository productRepository, CategoryRepository categoryRepository) {
		this.productRepository = productRepository;
		this.categoryRepository = categoryRepository;
	}

	@Override
	public Category getCategory(Long idCategory) {
		return categoryRepository.getCategoryById(idCategory);
	}

	@Override
	public Category createCategory(Category category) {
		return categoryRepository.saveCategory(category);
	}

	@Override
	public void deleteCategory(Long idCategory) {
		if (categoryRepository.countSubCategories(idCategory) == 0 && productRepository.countProductsByCategory(idCategory) == 0)			
			categoryRepository.deleteCategory(idCategory);		
		else
			throw new CategoryNotRemovableException(CATEGORY_NO_REMOVABLE);
	}

	@Override
	public Page<Category> getSuperCategories(Pageable pageable) {
		return categoryRepository.getCategoriesBySuperCategory(null, pageable);
	}

	@Override
	public Page<Category> getSubCategories(Long idCategory, Pageable pageable) {
		return categoryRepository.getCategoriesBySuperCategory(idCategory, pageable);
	}

}
