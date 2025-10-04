package com.eipl.amcs.master.operation.bootcontroller;

import com.eipl.amcs.master.operation.model.Formula;
import com.eipl.amcs.master.operation.repository.FormulaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("formula")
public class FormulaController {

	@Autowired
	private FormulaRepository repository;

	private static final Logger LOGGER = LoggerFactory.getLogger(FormulaController.class);

	@GetMapping
	public ResponseEntity<List<Formula>> index() {
		try {
			return new ResponseEntity<List<Formula>>(repository.findAll(), HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
