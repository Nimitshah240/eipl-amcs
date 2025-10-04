package com.eipl.amcs.master.geo.bootcontroller;

import com.eipl.amcs.master.geo.model.SubDistrict;
import com.eipl.amcs.master.geo.service.SubDistrictService;
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
@RequestMapping("/sub-districts")
public class SubDistrictController {

	@Autowired
	private SubDistrictService service;

	private static final Logger LOGGER = LoggerFactory.getLogger(SubDistrictController.class);

	@GetMapping
	public ResponseEntity<List<SubDistrict>> index(
			@RequestParam(name = "districtCode", required = false) String districtCode) {
		try {
			List<SubDistrict> list;
			if (districtCode == null || districtCode.isEmpty())
				list = service.findAll();
			else
				list = service.findAll(districtCode);
			if (list == null || list.isEmpty())
				return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

			return new ResponseEntity<List<SubDistrict>>(list, HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
