package com.perfiltic.ecommerce.domain.exceptions;

public class CategoryIsNotLastLevelException extends RuntimeException {

	private static final long serialVersionUID = -1205549552871487562L;

	public CategoryIsNotLastLevelException(String message) {
		super(message);
	}
}
