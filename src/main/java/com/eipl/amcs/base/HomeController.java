package com.eipl.amcs.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/home")
public class HomeController {

	private static final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);

	@GetMapping
	public ResponseEntity<String> healthCheck(@RequestHeader Map<String, String> headers) {
		headers.forEach((k, v) -> {
			LOGGER.info("Header {} = {}", k, v);
		});
		return new ResponseEntity<String>("OK", HttpStatus.OK);
	}
}
