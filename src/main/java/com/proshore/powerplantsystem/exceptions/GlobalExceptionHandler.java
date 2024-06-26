package com.proshore.powerplantsystem.exceptions;

import com.proshore.powerplantsystem.payloads.APIResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * The GlobalExceptionHandler class is responsible for handling various exceptions that may
 * occur during API requests and providing appropriate responses. It uses Spring's exception
 * handling mechanisms to customize error responses for different types of exceptions.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

	/**
	 * Handles APIException instances by returning a Bad Request (400) HTTP response
	 * with the error message from the exception.
	 *
	 * @param e The APIException instance.
	 * @return A ResponseEntity containing an APIResponse with the error message.
	 */
	@ExceptionHandler(APIException.class)
	public ResponseEntity<APIResponse> apiException(APIException e) {
		String message = e.getMessage();
		APIResponse res = new APIResponse(message, false);
		return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Handles MissingPathVariableException instances by returning a Bad Request (400) HTTP response
	 * with the error message from the exception.
	 *
	 * @param e The MissingPathVariableException instance.
	 * @return A ResponseEntity containing an APIResponse with the error message.
	 */
	@ExceptionHandler(MissingPathVariableException.class)
	public ResponseEntity<APIResponse> missingPathVariableException(MissingPathVariableException e) {
		APIResponse res = new APIResponse(e.getMessage(), false);
		return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Handles DataNotFoundException instances by returning a Not Found (404) HTTP response
	 * with the error message from the exception.
	 *
	 * @param e The DataNotFoundException instance.
	 * @return A ResponseEntity containing an APIResponse with the error message.
	 */
	@ExceptionHandler(DataNotFoundException.class)
	public ResponseEntity<APIResponse> dataNotFoundException(DataNotFoundException e) {
		String message = e.getMessage();
		APIResponse res = new APIResponse(message, false);
		return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
	}

	/**
	 * Handles MethodArgumentNotValidException instances by returning a Bad Request (400) HTTP response
	 * with validation error messages for fields in the request.
	 *
	 * @param ex The MethodArgumentNotValidException instance.
	 * @return A Map containing field names as keys and validation error messages as values.
	 */
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String, String> handleValidationExceptions(
			MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});
		return errors;
	}
}
