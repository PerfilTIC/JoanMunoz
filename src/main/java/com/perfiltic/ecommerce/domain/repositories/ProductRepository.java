package com.perfiltic.ecommerce.domain.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.perfiltic.ecommerce.domain.model.Product;

public interface ProductRepository {
	
	public int countProductsByCategory(Long idCategory);
	
	public Page<Product> getProductsByCategory(Long idCategory, Pageable pageable);

	public Product getProductById(Long idProduct);
	
	public Product saveProduct(Product product);
	
	public void deleteProduct(Long idProduct); 
	
}
