package com.perfiltic.ecommerce.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_product")
	private Long idProduct;
	
	@NotNull
	private Long idCategory;	
	
	@NotNull
	private String name;
	
	@NotNull
	private String description;
	
	@NotNull
	private double weight;
	
	@NotNull
	private double price;
	
	private String picture1;
	
	private String picture2;
	
	private String picture3;
}
