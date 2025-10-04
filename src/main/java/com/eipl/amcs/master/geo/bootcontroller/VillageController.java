package com.eipl.amcs.master.geo.bootcontroller;

import com.eipl.amcs.master.geo.model.Village;
import com.eipl.amcs.master.geo.service.VillageService;
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
@RequestMapping("/villages")
public class VillageController {

	@Autowired
	private VillageService service;

	private static final Logger LOGGER = LoggerFactory.getLogger(VillageController.class);

	@GetMapping
	public ResponseEntity<List<Village>> index(
			@RequestParam(name = "subDistrictCode", required = false) String subDistrictCode) {
		try {
			List<Village> list;
			if (subDistrictCode == null || subDistrictCode.isEmpty())
				list = service.findAll();
			else
				list = service.findAll(subDistrictCode);
			
			if (list == null || list.isEmpty())
				return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

			return new ResponseEntity<List<Village>>(list, HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
