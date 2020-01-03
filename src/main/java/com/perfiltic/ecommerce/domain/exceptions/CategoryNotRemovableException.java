package com.perfiltic.ecommerce.domain.exceptions;

public class CategoryNotRemovableException extends RuntimeException {

	private static final long serialVersionUID = 7079308531082948749L;

	public CategoryNotRemovableException(String message) {
		super(message);
	}
}
