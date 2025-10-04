package com.eipl.amcs.master.global.bootcontroller;

import com.eipl.amcs.master.global.model.Shift;
import com.eipl.amcs.master.global.service.ShiftService;
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
@RequestMapping("/shifts")
public class ShiftController {

	@Autowired
	private ShiftService service;

	private static final Logger LOGGER = LoggerFactory.getLogger(ShiftController.class);

	@GetMapping
	public ResponseEntity<List<Shift>> index() {
		try {
			List<Shift> list = service.findAll();
			if (list == null || list.isEmpty())
				return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

			return new ResponseEntity<List<Shift>>(list, HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
