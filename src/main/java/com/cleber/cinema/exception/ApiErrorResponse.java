package com.cleber.cinema.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiErrorResponse {
	private int statusCode;
	private String message;
	private LocalDateTime timestamp;
}
