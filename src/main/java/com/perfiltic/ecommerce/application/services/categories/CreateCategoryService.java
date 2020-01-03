package com.perfiltic.ecommerce.application.services.categories;

import java.io.IOException;

import com.perfiltic.ecommerce.domain.model.Category;

public interface CreateCategoryService {

	public Category createCategory(Category category) throws IOException;
}
