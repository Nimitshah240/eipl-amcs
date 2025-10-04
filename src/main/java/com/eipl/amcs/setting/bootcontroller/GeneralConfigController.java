package com.eipl.amcs.setting.bootcontroller;

import com.eipl.amcs.setting.model.GeneralConfig;
import com.eipl.amcs.setting.service.GeneralConfigService;
import com.eipl.amcs.util.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/general-config")
public class GeneralConfigController {

	@Autowired
	private GeneralConfigService service;


	private static final Logger LOGGER = LoggerFactory.getLogger(GeneralConfigController.class);

	@GetMapping
	public ResponseEntity<List<GeneralConfig>> index() {
		try {
			List<GeneralConfig> list = service.findAll();
			if (list == null || list.isEmpty())
				return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

			return new ResponseEntity<List<GeneralConfig>>(list, HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping
	public ResponseEntity<List<GeneralConfig>> create(@RequestHeader Map<String, String> headers,@RequestBody List<GeneralConfig> dto) {
		return new ResponseEntity<List<GeneralConfig>>(service.save(dto,CommonUtil.getIdentityHeader(headers)), HttpStatus.CREATED);
	}
	
	@PutMapping
	public ResponseEntity<GeneralConfig> updateGeneralConfigWithMilkType(@RequestHeader Map<String, String> headers,@RequestBody GeneralConfig dto) {
		try {
			LOGGER.info("GeneralConfig save method");
			dto = service.update(dto,CommonUtil.getIdentityHeader(headers));
			if (dto == null)
				return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
			
			return new ResponseEntity<>(dto, HttpStatus.CREATED);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping("/{code}")
	public ResponseEntity<?> deleteGeneralConfig(@RequestHeader Map<String, String> headers,@PathVariable("code") String code) {
		try {
			LOGGER.info("GeneralConfig delete method");
			Optional<GeneralConfig> objectData = service.findById(code); 
			if (objectData == null || !objectData.isPresent())
				return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
			
			service.delete(objectData.get(),CommonUtil.getIdentityHeader(headers));
			return new ResponseEntity<>(null, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
