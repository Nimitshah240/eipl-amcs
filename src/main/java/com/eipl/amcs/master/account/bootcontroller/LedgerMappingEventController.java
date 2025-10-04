package com.eipl.amcs.master.account.bootcontroller;

import com.eipl.amcs.master.account.model.LedgerMappingEvent;
import com.eipl.amcs.master.account.service.LedgerMappingEventService;
import com.eipl.amcs.util.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ledger-mapping-event")
public class LedgerMappingEventController {

	@Autowired
	private LedgerMappingEventService service;

	private static final Logger LOGGER = LoggerFactory.getLogger(LedgerMappingEventController.class);

	@GetMapping
	public ResponseEntity<List<LedgerMappingEvent>> index() {
		try {
			List<LedgerMappingEvent> list = service.findAll();
			if (list == null || list.isEmpty())
				return new ResponseEntity<>(null, HttpStatus.OK);
			
			return new ResponseEntity<List<LedgerMappingEvent>>(list, HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


	@PostMapping
	public ResponseEntity<String> createEvent(@RequestHeader Map<String, String> headers, @RequestBody List<LedgerMappingEvent> dto) {
		return new ResponseEntity<>(service.save(dto, CommonUtil.getIdentityHeader(headers)), HttpStatus.CREATED);
	}

}
