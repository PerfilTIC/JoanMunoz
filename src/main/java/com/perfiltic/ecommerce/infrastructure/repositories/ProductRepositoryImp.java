package com.perfiltic.ecommerce.infrastructure.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.perfiltic.ecommerce.domain.model.Product;
import com.perfiltic.ecommerce.domain.repositories.ProductRepository;
import com.perfiltic.ecommerce.infrastructure.daos.ProductRepositoryDao;

@Repository
public class ProductRepositoryImp implements ProductRepository {

	@Autowired
	private ProductRepositoryDao productRepositoryDao;

	@Override
	public Page<Product> getProductsByCategory(Long idCategory, Pageable pageable) {
		return  productRepositoryDao.findByIdCategory(idCategory, pageable);
	}

	@Override
	public int countProductsByCategory(Long idCategory) {
		return productRepositoryDao.countByIdCategory(idCategory);
	}

	@Override
	public Product getProductById(Long idProduct) {
		return productRepositoryDao.findById(idProduct).orElse(null);
	}

	@Override
	public Product saveProduct(Product product) {
		return productRepositoryDao.save(product);
	}

	@Override
	public void deleteProduct(Long idProduct) {
		productRepositoryDao.deleteById(idProduct);
	}
}
