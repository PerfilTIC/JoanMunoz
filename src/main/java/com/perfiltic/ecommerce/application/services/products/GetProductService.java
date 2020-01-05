package com.perfiltic.ecommerce.application.services.products;

import com.perfiltic.ecommerce.domain.model.Product;

public interface GetProductService {

	public Product getProductById(Long idProduct);
}
