package com.eipl.amcs.master.org.bootcontroller;

import com.eipl.amcs.base.service.NextCodeService;
import com.eipl.amcs.master.org.dto.DockMilkTypeDto;
import com.eipl.amcs.master.org.model.Dock;
import com.eipl.amcs.master.org.service.DockService;
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
@RequestMapping("/docks")
public class DockController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DockController.class);
    @Autowired
    private DockService service;
    @Autowired
    private NextCodeService nextCodeService;

    @GetMapping
    public ResponseEntity<List<DockMilkTypeDto>> index() {
        try {
            List<DockMilkTypeDto> list = service.findAll();
            if (list == null || list.isEmpty())
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

            return new ResponseEntity<List<DockMilkTypeDto>>(list, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/next-code")
    public ResponseEntity<String> nextCode(@RequestParam(name = "society", required = true) String societyCode) {
        try {
            LOGGER.info("Next dock no for Society: {}", societyCode);
            String code = nextCodeService.getNextCode("Dock", "dockNo", societyCode, 2);
            if (code == null || code.isEmpty())
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

            return new ResponseEntity<>(code, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<DockMilkTypeDto> createDockWithMilkType(@RequestHeader Map<String, String> headers,
                                                                  @RequestBody DockMilkTypeDto dto) {
        try {
            LOGGER.info("Dock save method");
            dto = service.save(dto, CommonUtil.getIdentityHeader(headers));
            if (dto == null)
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

            return new ResponseEntity<>(dto, HttpStatus.CREATED);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping
    public ResponseEntity<DockMilkTypeDto> updateDockWithMilkType(@RequestHeader Map<String, String> headers,
                                                                  @RequestBody DockMilkTypeDto dto) {
        try {
            LOGGER.info("Dock save method");
            dto = service.update(dto, CommonUtil.getIdentityHeader(headers));
            if (dto == null)
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

            return new ResponseEntity<>(dto, HttpStatus.CREATED);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{dockNo}")
    public ResponseEntity<?> deleteDock(@RequestHeader Map<String, String> headers, @PathVariable("dockNo") String dockNo) {
        try {
            LOGGER.info("Dock delete method");
            Optional<Dock> dockData = service.findById(dockNo);
            if (dockData == null || !dockData.isPresent())
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

            service.delete(dockData.get(), CommonUtil.getIdentityHeader(headers));
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
