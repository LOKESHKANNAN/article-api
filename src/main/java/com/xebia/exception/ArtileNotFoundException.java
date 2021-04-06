package com.xebia.exception;

public class ArtileNotFoundException extends RuntimeException {

	public ArtileNotFoundException(String errorMessage) {
		super(errorMessage);
	}

}
