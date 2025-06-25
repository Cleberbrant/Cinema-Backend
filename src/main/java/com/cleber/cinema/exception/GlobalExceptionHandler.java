package com.cleber.cinema.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ApiErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
		ApiErrorResponse error = new ApiErrorResponse(
				HttpStatus.NOT_FOUND.value(),
				ex.getMessage(),
				LocalDateTime.now());
		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(ValidationException.class)
	public ResponseEntity<ApiErrorResponse> handleValidationException(ValidationException ex) {
		ApiErrorResponse error = new ApiErrorResponse(
				HttpStatus.BAD_REQUEST.value(),
				ex.getMessage(),
				LocalDateTime.now());
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<ApiErrorResponse> handleBusinessException(BusinessException ex) {
		ApiErrorResponse error = new ApiErrorResponse(
				HttpStatus.CONFLICT.value(),
				ex.getMessage(),
				LocalDateTime.now());
		return new ResponseEntity<>(error, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<Map<String, String>> handleConstraintViolationException(ConstraintViolationException ex) {
		Map<String, String> errors = new HashMap<>();
		ex.getConstraintViolations().forEach(violation -> {
			String fieldName = violation.getPropertyPath().toString();
			String errorMessage = violation.getMessage();
			errors.put(fieldName, errorMessage);
		});
		return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(TransactionSystemException.class)
	public ResponseEntity<?> handleTransactionSystemException(TransactionSystemException ex) {
		Throwable rootCause = ex.getRootCause();
		if (rootCause instanceof ConstraintViolationException) {
			return handleConstraintViolationException((ConstraintViolationException) rootCause);
		}
		return handleGeneralException(ex);
	}

	@ExceptionHandler({AccessDeniedException.class, AuthorizationDeniedException.class})
	public ResponseEntity<ApiErrorResponse> handle403Exception(Exception ex) {
		ApiErrorResponse error = new ApiErrorResponse(
				HttpStatus.FORBIDDEN.value(),
				"Acesso negado: você não tem permissão para acessar este recurso.",
				LocalDateTime.now());
		return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
	}

	// Opcional: Tratamento para NullPointerException (evita que NPE vire 500)
	@ExceptionHandler(NullPointerException.class)
	public ResponseEntity<ApiErrorResponse> handleNullPointerException(NullPointerException ex) {
		ApiErrorResponse error = new ApiErrorResponse(
				HttpStatus.BAD_REQUEST.value(),
				"Operação inválida: dados obrigatórios não informados.",
				LocalDateTime.now());
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiErrorResponse> handleGeneralException(Exception ex) {
		ApiErrorResponse error = new ApiErrorResponse(
				HttpStatus.INTERNAL_SERVER_ERROR.value(),
				"Ocorreu um erro interno. Por favor, tente novamente mais tarde.",
				LocalDateTime.now());
		return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = error instanceof FieldError ? ((FieldError) error).getField() : error.getObjectName();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});
		return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ApiErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
		ApiErrorResponse error = new ApiErrorResponse(
				HttpStatus.BAD_REQUEST.value(),
				ex.getMessage(),
				LocalDateTime.now());
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

}
