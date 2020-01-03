package com.perfiltic.ecommerce.domain.exceptions;

public class CategorynotLastLevelException extends RuntimeException {

	private static final long serialVersionUID = -1205549552871487562L;

	public CategorynotLastLevelException(String message) {
		super(message);
	}
}
