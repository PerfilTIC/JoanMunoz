package com.perfiltic.ecommerce.application.services.products;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.perfiltic.ecommerce.domain.model.Product;

public interface GetProductsByCategoryService {

	public Page<Product> getProductsByCategory(Long idCategory, Pageable pageable);
}
