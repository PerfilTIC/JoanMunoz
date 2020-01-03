package com.perfiltic.ecommerce.infrastructure.daos;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.perfiltic.ecommerce.domain.model.Category;

public interface CategoryRepositoryDao extends PagingAndSortingRepository<Category, Long> {

	public Page<Category> findBySuperCategory(Long idCategory, Pageable pageable);
	
	public int countBySuperCategory(Long superCategory);
}
