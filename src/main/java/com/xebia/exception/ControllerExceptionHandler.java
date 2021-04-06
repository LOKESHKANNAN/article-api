package com.xebia.exception;


import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class ControllerExceptionHandler {
	@ExceptionHandler(BadRequestException.class)

	public ResponseEntity<ErrorMessage> BadRequestException(BadRequestException exception, WebRequest request){
		ErrorMessage message = new ErrorMessage(
				HttpStatus.BAD_REQUEST.value(),
				new Date(),
				exception.getMessage(),
				request.getDescription(false));
		return new ResponseEntity<ErrorMessage>(message, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(ArtileNotFoundException.class)
	public ResponseEntity<ErrorMessage> ArtileNotFoundException(ArtileNotFoundException exception, WebRequest request){
		ErrorMessage message = new ErrorMessage(
				HttpStatus.NOT_FOUND.value(),
				new Date(),
				exception.getMessage(),
				request.getDescription(false));
		return new ResponseEntity<ErrorMessage>(message, HttpStatus.NOT_FOUND);
	}
}