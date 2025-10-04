package com.eipl.amcs.master.operation.bootcontroller;

import com.eipl.amcs.base.service.NextCodeService;
import com.eipl.amcs.exception.BusinessValidationFailException;
import com.eipl.amcs.master.operation.dto.MemberImportDto;
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.master.operation.model.MemberDetail;
import com.eipl.amcs.master.operation.model.MemberDto;
import com.eipl.amcs.master.operation.repository.MemberDetailRepository;
import com.eipl.amcs.master.operation.repository.MemberRepository;
import com.eipl.amcs.master.operation.service.MemberService;
import com.eipl.amcs.operation.procurement.dto.MemberSocietyInfoDto;
import com.eipl.amcs.util.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/members")
public class MemberController {

    @Autowired
    private MemberService service;
    @Autowired
    private NextCodeService nextCodeService;
    @Autowired
    private MemberRepository repository;
    @Autowired
    private MemberDetailRepository detailRepository;

    private static final DateTimeFormatter DATE_TIME_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
    private static final Logger LOGGER = LoggerFactory.getLogger(MemberController.class);

    @GetMapping
    public ResponseEntity<List<Member>> index(@RequestParam(name = "society", required = false) String societyCode) {
        try {
            List<Member> list = null;
            if (societyCode == null || societyCode.isEmpty())
                list = service.findAll();
            else
                list = service.findAllBySociety(societyCode);
            if (list == null || list.isEmpty())
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

            return new ResponseEntity<List<Member>>(list, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{code}")
    public ResponseEntity<Member> findByMemberCode(@PathVariable("code") String code) {
        return new ResponseEntity<>(service.findByMemberCode(code), HttpStatus.OK);
    }

    @GetMapping("/next-code")
    public ResponseEntity<String> nextCode(@RequestParam(name = "society", required = true) String societyCode) {
        try {
            LOGGER.info("Next member no for Society: {}", societyCode);
            String code = nextCodeService.getNextCode("Member", "code", societyCode, 4);
            if (code == null || code.isEmpty())
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

            return new ResponseEntity<>(code, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/member-detail/{code}")
    public ResponseEntity<MemberDetail> fetchMemberDetail(@PathVariable(name = "code", required = true) String code) {
        Member member = service.findByMemberCode(code);
        return new ResponseEntity<MemberDetail>(service.findDetailByMember(member), HttpStatus.OK);
    }

    @GetMapping("/member-details")
    public ResponseEntity<List<MemberDetail>> fetchMemberDetails() {
//		Member member = service.findByMemberCode(code);
        return new ResponseEntity<List<MemberDetail>>(service.findAllMemberDetails(), HttpStatus.OK);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> fetchMemberCount() {
        return new ResponseEntity<>(repository.count(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<MemberDto> createMember(@RequestHeader Map<String, String> headers,
                                                  @RequestBody MemberDto dto) throws BusinessValidationFailException {
        MemberDto dtoNew = service.save(dto, CommonUtil.getIdentityHeader(headers));

        dtoNew.getMember().setSociety(dto.getMember().getSociety());
        dtoNew.getMember().setMilkType(dto.getMember().getMilkType());
        dtoNew.getMember().setMemberType(dto.getMember().getMemberType());

        dtoNew.getMemberDetail().setGender(dto.getMemberDetail().getGender());
        dtoNew.getMemberDetail().setBank(dto.getMemberDetail().getBank());
        dtoNew.getMemberDetail().setBranch(dto.getMemberDetail().getBranch());
        dtoNew.getMemberDetail().setState(dto.getMemberDetail().getState());
        dtoNew.getMemberDetail().setDistrict(dto.getMemberDetail().getDistrict());
        dtoNew.getMemberDetail().setSubDistrict(dto.getMemberDetail().getSubDistrict());
        dtoNew.getMemberDetail().setVillage(dto.getMemberDetail().getVillage());
        dtoNew.getMemberDetail().setHamlet(dto.getMemberDetail().getHamlet());
        dtoNew.getMemberDetail().setMember(dto.getMemberDetail().getMember());

        return new ResponseEntity<>(dtoNew, HttpStatus.CREATED);
    }

    @PostMapping("/import")
    public ResponseEntity<List<MemberImportDto>> importMember(@RequestHeader Map<String, String> headers,
                                                              @RequestBody List<MemberDto> dtoList) {
        return new ResponseEntity<List<MemberImportDto>>(
                service.importMembers(dtoList, CommonUtil.getIdentityHeader(headers)), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<MemberDto> updateMember(@RequestHeader Map<String, String> headers,
                                                  @RequestBody MemberDto dto) {
        try {
            MemberDto dtoNew = service.update(dto, CommonUtil.getIdentityHeader(headers));

            dtoNew.getMember().setSociety(dto.getMember().getSociety());
            dtoNew.getMember().setMilkType(dto.getMember().getMilkType());
            dtoNew.getMember().setMemberType(dto.getMember().getMemberType());

            dtoNew.getMemberDetail().setGender(dto.getMemberDetail().getGender());
            dtoNew.getMemberDetail().setBank(dto.getMemberDetail().getBank());
            dtoNew.getMemberDetail().setBranch(dto.getMemberDetail().getBranch());
            dtoNew.getMemberDetail().setState(dto.getMemberDetail().getState());
            dtoNew.getMemberDetail().setDistrict(dto.getMemberDetail().getDistrict());
            dtoNew.getMemberDetail().setSubDistrict(dto.getMemberDetail().getSubDistrict());
            dtoNew.getMemberDetail().setVillage(dto.getMemberDetail().getVillage());
            dtoNew.getMemberDetail().setHamlet(dto.getMemberDetail().getHamlet());
            dtoNew.getMemberDetail().setMember(dto.getMemberDetail().getMember());

            return new ResponseEntity<>(dtoNew, HttpStatus.CREATED);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<?> deleteMember(@RequestHeader Map<String, String> headers,
                                          @PathVariable("code") String code) {
        try {
            service.delete(code, CommonUtil.getIdentityHeader(headers));
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/member-information")
    public ResponseEntity<MemberSocietyInfoDto> fetchMemberInformation(
            @RequestParam(name = "code", required = true) String code,
            @RequestParam(name = "date", required = true) String date,
            @RequestParam(name = "count", required = false) Integer count,
            @RequestParam(name = "paymentCycle", required = false) String paymentCycle) {
        LocalDateTime dt = LocalDateTime.parse(date, DATE_TIME_FMT);
        if (count == null)
            count = 5;
        return new ResponseEntity<MemberSocietyInfoDto>(service.findMemberInformation(code, dt, count, paymentCycle), HttpStatus.OK);
    }

}
