package com.eipl.amcs.setting.bootcontroller;

import com.eipl.amcs.setting.model.HardwareDeviceConfig;
import com.eipl.amcs.setting.service.HardwareDeviceConfigService;
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
@RequestMapping("/hardware-device-config")
public class HardwareDeviceConfigController {

	@Autowired
	private HardwareDeviceConfigService service;


	private static final Logger LOGGER = LoggerFactory.getLogger(HardwareDeviceConfigController.class);

	@GetMapping
	public ResponseEntity<List<HardwareDeviceConfig>> index() {
		try {
			List<HardwareDeviceConfig> list = service.findAll();
			if (list == null || list.isEmpty())
				return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

			return new ResponseEntity<List<HardwareDeviceConfig>>(list, HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping
	public ResponseEntity<String> create(@RequestHeader Map<String, String> headers,@RequestBody List<HardwareDeviceConfig> dto) {
		return new ResponseEntity<String>(service.saveUpdate(dto,CommonUtil.getIdentityHeader(headers)), HttpStatus.CREATED);
	}
	
	@PutMapping
	public ResponseEntity<HardwareDeviceConfig> updateHardwareDeviceConfigWithMilkType(@RequestHeader Map<String, String> headers,@RequestBody HardwareDeviceConfig dto) {
		try {
			LOGGER.info("HardwareDeviceConfig save method");
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
	public ResponseEntity<?> deleteHardwareDeviceConfig(@RequestHeader Map<String, String> headers,@PathVariable("code") String code) {
		try {
			LOGGER.info("HardwareDeviceConfig delete method");
			Optional<HardwareDeviceConfig> hardwareDeviceConfigData = service.findById(code); 
			if (hardwareDeviceConfigData == null || !hardwareDeviceConfigData.isPresent())
				return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
			
			service.delete(hardwareDeviceConfigData.get(),CommonUtil.getIdentityHeader(headers));
			return new ResponseEntity<>(null, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
