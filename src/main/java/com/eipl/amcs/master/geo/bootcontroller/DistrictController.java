package com.eipl.amcs.master.geo.bootcontroller;

import com.eipl.amcs.master.geo.model.District;
import com.eipl.amcs.master.geo.service.DistrictService;
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
@RequestMapping("/districts")
public class DistrictController {

	@Autowired
	private DistrictService service;

	private static final Logger LOGGER = LoggerFactory.getLogger(DistrictController.class);

	@GetMapping
	public ResponseEntity<List<District>> index(@RequestParam(name = "stateCode", required = false) String stateCode) {
		try {
			List<District> list;
			if (stateCode == null || stateCode.isEmpty())
				list = service.findAll();
			else
				list = service.findAll(stateCode);
			
			if (list == null || list.isEmpty())
				return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

			return new ResponseEntity<List<District>>(list, HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
