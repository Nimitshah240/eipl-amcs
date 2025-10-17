package com.eipl.amcs.master.account.bootcontroller;

import com.eipl.amcs.base.service.NextCodeService;
import com.eipl.amcs.master.account.model.MeetingAgenda;
import com.eipl.amcs.master.account.model.Mom;
import com.eipl.amcs.master.account.model.MomAction;
import com.eipl.amcs.master.account.service.MeetingAgendaService;
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
@RequestMapping("/meeting-agenda")
public class MeetingAgendaController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MeetingAgendaController.class);
    @Autowired
    private MeetingAgendaService service;
    @Autowired
    private NextCodeService nextCodeService;

    @GetMapping
    public ResponseEntity<List<MeetingAgenda>> index() {
        try {
            List<MeetingAgenda> list = service.findAll();
            if (list == null || list.isEmpty())
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            return new ResponseEntity<List<MeetingAgenda>>(list, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/mom/{code}")
    public ResponseEntity<List<Mom>> indexMom(@PathVariable(name = "code") String code) {
        try {
            List<Mom> list = service.findMom(code);
            if (list == null || list.isEmpty())
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            return new ResponseEntity<List<Mom>>(list, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/mom_action/{code}")
    public ResponseEntity<List<MomAction>> indexMomAction(@PathVariable String code) {
        try {
            List<MomAction> list = service.findMomAction(code);
            if (list == null || list.isEmpty())
                return new ResponseEntity<>(list, HttpStatus.NO_CONTENT);
            return new ResponseEntity<List<MomAction>>(list, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/next-code")
    public ResponseEntity<String> nextCode(@RequestParam(name = "society", required = true) String societyCode) {
        try {
            LOGGER.info("Next product no for MeetingAgenda: {}", societyCode);
            String code = nextCodeService.getNextCode("MeetingAgenda", "code", societyCode, 2);
            if (code == null || code.isEmpty())
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

            return new ResponseEntity<>(code, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<MeetingAgenda> createMeetingAgenda(@RequestHeader Map<String, String> headers, @RequestBody MeetingAgenda dto) {
        return new ResponseEntity<>(service.save(dto, CommonUtil.getIdentityHeader(headers)), HttpStatus.CREATED);

    }

    @PostMapping("/mom")
    public ResponseEntity<Mom> createMom(@RequestHeader Map<String, String> headers, @RequestBody Mom dto) {
        return new ResponseEntity<>(service.saveMom(dto, CommonUtil.getIdentityHeader(headers)), HttpStatus.CREATED);

    }

    @PostMapping("/mom_action")
    public ResponseEntity<MomAction> createMomAction(@RequestHeader Map<String, String> headers, @RequestBody MomAction dto) {
        return new ResponseEntity<>(service.saveMomAction(dto, CommonUtil.getIdentityHeader(headers)), HttpStatus.CREATED);

    }

    @PutMapping
    public ResponseEntity<MeetingAgenda> updateMeetingAgenda(@RequestHeader Map<String, String> headers, @RequestBody MeetingAgenda dto) {

        try {
            LOGGER.info("MeetingAgenda save method");
            dto = service.update(dto, CommonUtil.getIdentityHeader(headers));
            if (dto == null)
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            return new ResponseEntity<>(dto, HttpStatus.CREATED);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/mom")
    public ResponseEntity<Mom> updateMom(@RequestHeader Map<String, String> headers, @RequestBody Mom dto) {

        try {
            LOGGER.info("Mom save method");
            dto = service.updateMom(dto, CommonUtil.getIdentityHeader(headers));
            if (dto == null)
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            return new ResponseEntity<>(dto, HttpStatus.CREATED);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/mom_action")
    public ResponseEntity<MomAction> updateMeetingAgenda(@RequestHeader Map<String, String> headers, @RequestBody MomAction dto) {
        try {
            LOGGER.info("MomAction save method");
            dto = service.updateMomAction(dto, CommonUtil.getIdentityHeader(headers));
            if (dto == null)
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            return new ResponseEntity<>(dto, HttpStatus.CREATED);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<?> deleteMeetingAgenda(@RequestHeader Map<String, String> headers,
                                                 @PathVariable("code") String code) {
        service.delete(code, CommonUtil.getIdentityHeader(headers));
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @DeleteMapping("/mom/{code}")
    public ResponseEntity<?> deleteMom(@RequestHeader Map<String, String> headers,
                                       @PathVariable("code") String code) {
        service.deleteMom(code, CommonUtil.getIdentityHeader(headers));
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @DeleteMapping("/mom_action/{code}")
    public ResponseEntity<?> deleteMomAction(@RequestHeader Map<String, String> headers,
                                             @PathVariable("code") String code) {
        service.deleteMomAction(code, CommonUtil.getIdentityHeader(headers));
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

}

