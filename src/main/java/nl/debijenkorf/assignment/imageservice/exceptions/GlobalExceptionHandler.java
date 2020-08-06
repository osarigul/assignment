package nl.debijenkorf.assignment.imageservice.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler
	public ResponseEntity handleExceptions(Exception e) {

		LOGGER.error("An exception caught, returning 404", e);

		return ResponseEntity
				.status(HttpStatus.NOT_FOUND)
				.build();
	}
}
