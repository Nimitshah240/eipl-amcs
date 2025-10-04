package com.eipl.amcs.master.account.bootcontroller;

import com.eipl.amcs.base.service.NextCodeService;
import com.eipl.amcs.master.account.model.CommitteeMembers;
import com.eipl.amcs.master.account.service.CommitteeMembersService;
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
@RequestMapping("/committee-members")
public class CommitteeMembersController {

    @Autowired
    private CommitteeMembersService service;

    @Autowired
    private NextCodeService nextCodeService;

    private static final Logger LOGGER = LoggerFactory.getLogger(CommitteeMembersController.class);

    @GetMapping
    public ResponseEntity<List<CommitteeMembers>> index(String memberCode) {
        try {
            List<CommitteeMembers> list = service.findAll();
            if (list == null || list.isEmpty())
                list = service.findAll();
            else
                list = service.findAll();
            if (list == null || list.isEmpty())
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            return new ResponseEntity<List<CommitteeMembers>>(list, HttpStatus.OK);

        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //	@GetMapping("/next-code")
//	public ResponseEntity<String> nextCode(String memberCode) {
//		try {
//			LOGGER.info("Next product no for Society: {}", memberCode);
//			String code = nextCodeService.getNextCode("CommitteeMembers", "code", memberCode, 0);
//			if (code == null || code.isEmpty())
//				return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
//
//			return new ResponseEntity<>(code, HttpStatus.OK);
//		} catch (Exception e) {
//			LOGGER.error(e.getMessage());
//			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//	}
    @PostMapping
    public ResponseEntity<CommitteeMembers> createCommitteeMember(@RequestHeader Map<String, String> headers, @RequestBody CommitteeMembers dto) {

        return new ResponseEntity<>(service.save(dto, CommonUtil.getIdentityHeader(headers)), HttpStatus.CREATED);

    }

    @PutMapping
    public ResponseEntity<CommitteeMembers> updateCommitteMembers(@RequestHeader Map<String, String> headers, @RequestBody CommitteeMembers dto) {

        try {
            LOGGER.info("CommitteeMembers save method");
            dto = service.update(dto, CommonUtil.getIdentityHeader(headers));
            if (dto == null)
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            return new ResponseEntity<>(dto, HttpStatus.CREATED);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<?> deleteCommitteeMembers(@RequestHeader Map<String, String> headers,
                                                    @PathVariable("code") String code) {
        service.delete(code, CommonUtil.getIdentityHeader(headers));
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}


