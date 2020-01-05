package com.perfiltic.ecommerce.application.services.categories;

import java.io.IOException;

import com.perfiltic.ecommerce.domain.model.Category;

public interface SaveCategoryService {

	public Category saveCategory(Category category) throws IOException;
}
