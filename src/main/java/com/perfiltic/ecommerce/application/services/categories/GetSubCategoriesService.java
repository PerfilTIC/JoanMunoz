package com.perfiltic.ecommerce.application.services.categories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.perfiltic.ecommerce.domain.model.Category;

public interface GetSubCategoriesService {

	public Page<Category> getSubCategories(Long idCategory, Pageable pageable);
}
