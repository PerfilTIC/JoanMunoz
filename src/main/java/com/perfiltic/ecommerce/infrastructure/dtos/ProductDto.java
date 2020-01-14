package com.perfiltic.ecommerce.infrastructure.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

	private Long idProduct;
	private Long idCategory;	
	private String name;
	private String description;
	private double weight;
	private double price;
}
