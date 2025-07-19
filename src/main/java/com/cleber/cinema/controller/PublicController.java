package com.cleber.cinema.controller;

import com.cleber.cinema.dto.CinemaDTO;
import com.cleber.cinema.services.CinemaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/public") // ← URL DIFERENTE DE /api/*
@RequiredArgsConstructor
public class PublicController {

	private final CinemaService cinemaService;

	@GetMapping("/test")
	public Map<String, Object> test() {
		return Map.of(
				"message", "API pública funcionando!",
				"timestamp", System.currentTimeMillis()
		);
	}

	@GetMapping("/cinemas")
	public List<CinemaDTO> getCinemas() {
		return cinemaService.findAll();
	}
}