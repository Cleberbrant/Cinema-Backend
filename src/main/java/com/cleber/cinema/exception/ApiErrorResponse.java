package com.cleber.cinema.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ApiErrorResponse {
	private int statusCode;
	private String message;
	private LocalDateTime timestamp;
}
