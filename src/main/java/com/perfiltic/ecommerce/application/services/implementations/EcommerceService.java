package com.perfiltic.ecommerce.application.services.implementations;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.perfiltic.ecommerce.application.services.categories.DeleteCategoryService;
import com.perfiltic.ecommerce.application.services.categories.GetCategoryService;
import com.perfiltic.ecommerce.application.services.categories.GetSubCategoriesService;
import com.perfiltic.ecommerce.application.services.categories.GetSuperCategoriesService;
import com.perfiltic.ecommerce.application.services.categories.SaveCategoryService;
import com.perfiltic.ecommerce.application.services.products.DeleteProductService;
import com.perfiltic.ecommerce.application.services.products.GetProductService;
import com.perfiltic.ecommerce.application.services.products.GetProductsByCategoryService;
import com.perfiltic.ecommerce.application.services.products.SaveProductService;
import com.perfiltic.ecommerce.domain.exceptions.CategoryIsNotLastLevelException;
import com.perfiltic.ecommerce.domain.exceptions.CategoryNotRemovableException;
import com.perfiltic.ecommerce.domain.model.Category;
import com.perfiltic.ecommerce.domain.model.Product;
import com.perfiltic.ecommerce.domain.repositories.CategoryRepository;
import com.perfiltic.ecommerce.domain.repositories.ProductRepository;

@Service
public class EcommerceService
		implements GetCategoryService, SaveCategoryService, DeleteCategoryService, GetSubCategoriesService,
		GetSuperCategoriesService, GetProductService, GetProductsByCategoryService, DeleteProductService, SaveProductService {

	public static final String CATEGORY_NO_REMOVABLE = "The category is not removable because it has some products or subcategories.";
	public static final String CATEGORY_IS_NOT_FINAL = "The category cannot have products because it has subcategories.";
	
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
	public Page<Category> getSuperCategories(Pageable pageable) {
		return categoryRepository.getCategoriesBySuperCategory(null, pageable);
	}

	@Override
	public Page<Category> getSubCategories(Long idCategory, Pageable pageable) {
		return categoryRepository.getCategoriesBySuperCategory(idCategory, pageable);
	}

	@Override
	public Category saveCategory(Category category) {
		return categoryRepository.saveCategory(category);
	}

	@Override
	public void deleteCategory(Long idCategory) {
		if (categoryRepository.countSubCategories(idCategory) == 0
				&& productRepository.countProductsByCategory(idCategory) == 0)
			categoryRepository.deleteCategory(idCategory);
		else
			throw new CategoryNotRemovableException(CATEGORY_NO_REMOVABLE);
	}

	@Override
	public Product getProductById(Long idProduct) {
		return productRepository.getProductById(idProduct);
	}

	@Override
	public Page<Product> getProductsByCategory(Long idCategory, Pageable pageable) {
		return productRepository.getProductsByCategory(idCategory, pageable);
	}

	@Override
	public void deleteProduct(Long idProduct) {
		productRepository.deleteProduct(idProduct);
	}

	@Override
	public Product saveProduct(Product product) {
		Category category = categoryRepository.getCategoryById(product.getIdCategory());
		
		if(categoryRepository.countSubCategories(category.getIdCategory()) == 0)
			return productRepository.saveProduct(product);
		else
			throw new CategoryIsNotLastLevelException(CATEGORY_IS_NOT_FINAL);
	}

}
