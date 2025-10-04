package com.eipl.amcs.master.geo.bootcontroller;

import com.eipl.amcs.master.geo.model.Hamlet;
import com.eipl.amcs.master.geo.service.HamletService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/hamlets")
public class HamletController {

	@Autowired
	private HamletService service;

	private static final Logger LOGGER = LoggerFactory.getLogger(HamletController.class);

	@GetMapping
	public ResponseEntity<List<Hamlet>> index(
			@RequestParam(name = "villageCode", required = false) String villageCode) {
		try {
			List<Hamlet> list;
			if (villageCode == null || villageCode.isEmpty())
				list = service.findAll();
			else
				list = service.findAll(villageCode);

			if (list == null || list.isEmpty())
				return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

			return new ResponseEntity<List<Hamlet>>(list, HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
