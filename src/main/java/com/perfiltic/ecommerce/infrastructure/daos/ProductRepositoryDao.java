package com.perfiltic.ecommerce.infrastructure.daos;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.perfiltic.ecommerce.domain.model.Product;

@Repository
public interface ProductRepositoryDao extends PagingAndSortingRepository<Product, Long> {

	public Page<Product> findByIdCategory(Long idCategory, Pageable pageable);
	
	public int countByIdCategory(Long idCategory);
	
}
