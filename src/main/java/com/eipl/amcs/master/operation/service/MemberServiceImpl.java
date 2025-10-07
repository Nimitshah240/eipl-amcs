
package com.eipl.amcs.master.operation.service;

import com.eipl.amcs.base.repository.NextCodeRepository;
import com.eipl.amcs.base.service.NextCodeService;
import com.eipl.amcs.exception.BusinessValidationFailException;
import com.eipl.amcs.exception.EntityNotFoundException;
import com.eipl.amcs.master.account.repository.LedgerRepository;
import com.eipl.amcs.master.account.repository.LedgerSubLedgerMappingRepository;
import com.eipl.amcs.master.account.repository.SubLedgerLedgerConfigRepository;
import com.eipl.amcs.master.account.repository.SubLedgerRepository;
import com.eipl.amcs.master.geo.model.*;
import com.eipl.amcs.master.global.model.Shift;
import com.eipl.amcs.master.operation.dto.MemberImportDto;
import com.eipl.amcs.master.operation.model.*;
import com.eipl.amcs.master.operation.repository.MemberCreditLimitRepository;
import com.eipl.amcs.master.operation.repository.MemberCreditLimitTransactionRepository;
import com.eipl.amcs.master.operation.repository.MemberDetailRepository;
import com.eipl.amcs.master.operation.repository.MemberRepository;
import com.eipl.amcs.master.org.model.Bank;
import com.eipl.amcs.master.org.model.Branch;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.org.repository.SocietyRepository;
import com.eipl.amcs.master.procurement.model.SocietyPaymentCycle;
import com.eipl.amcs.master.procurement.repository.SocietyPaymentCycleRepository;
import com.eipl.amcs.operation.procurement.dto.MemberSocietyInfoDto;
import com.eipl.amcs.operation.procurement.model.MilkCollection;
import com.eipl.amcs.operation.procurement.repository.MilkCollectionRepository;
import com.eipl.amcs.report.dto.MemberRegister;
import com.eipl.amcs.util.CommonUtil;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class MemberServiceImpl implements MemberService {
    @Autowired
    private MemberRepository repository;
    @Autowired
    private MemberDetailRepository memberDetailrepository;
    @Autowired
    private MemberCreditLimitRepository memberCreditLimitRepository;
    @Autowired
    private NextCodeRepository nextCodeRepository;
    @Autowired
    private MemberCreditLimitTransactionRepository memberCreditLimitTxnRepository;
    @Autowired
    private MilkCollectionRepository collectionRepository;
    @Autowired
    private SocietyRepository socRepository;
    @Autowired
    private SocietyPaymentCycleRepository paymentCycleRepository;
    @Autowired
    private NextCodeService nextCodeService;
    @Autowired
    private LedgerRepository ledgerRepository;
    @Autowired
    private LedgerSubLedgerMappingRepository mappingRepository;
    @Autowired
    private SubLedgerRepository subLedgerRepository;
    @Autowired
    private SubLedgerLedgerConfigRepository subLedgerLedgerConfigRepository;


    @Override
    public List<Member> findAll() {
        return repository.findAll(Sort.by("code"));
    }

    @Override
    public List<Member> findAllBySociety(String societyCode) {
        Society society = socRepository.findById(societyCode)
                .orElseThrow(() -> new EntityNotFoundException(Society.class, "societycode", "invalid.society"));
        return repository.findAllBySociety(society, Sort.by("code"));
    }

    @Override
    @Transactional
    public MemberDto save(MemberDto memberDto, String identityInfo) {
        Optional<Member> memberData = repository.findById(memberDto.getMember().getCode());
        if (memberData.isPresent()) {
            throw new BusinessValidationFailException(Member.class,
                    CommonUtil.getFieldError("Member", "code", memberDto.getMember().getCode(), "code.not.valid"));
        }

        MemberDto memberDtoNew = new MemberDto();

        Member member = memberDto.getMember();
        member.setInitData();
        memberDtoNew.setMember(repository.customSave(member, identityInfo));

        MemberDetail memberDetail = memberDto.getMemberDetail();

        memberDetail.setState(memberDto.getMemberDetail().getState());

        memberDetail.setCode(member.getCode());
        memberDetail.setInitData();
        memberDtoNew.setMemberDetail(memberDetailrepository.customSave(memberDetail, identityInfo));

        // Credit limit
        if (memberDtoNew.getMember().getMemberType().getCode() == 1)
            setMemberCreditLimit(memberDtoNew.getMember(), memberDtoNew.getMemberDetail());


//        SubLedger subLedger = new SubLedger();
//        String code = nextCodeService.getNextCode("SubLedger", "code", member.getSociety().getCode(), 0);
//        subLedger.setCode(code);
//        subLedger.setReferenceCode(member.getCode());
//        subLedger.setType(member.getMemberType().getCode() == 1 ? (short) 1 : (short) 2);
//        subLedger.setName(member.getFirstName() + " " + member.getMiddleName() + " " + member.getLastName());
//        subLedger.setNameLocal(member.getFirstNameLocal() + " " + member.getMiddleNameLocal() + " " + member.getLastNameLocal());
//        subLedger.setSociety(member.getSociety());
//        subLedger.setUnionCode(member.getSociety().getUnion() != null ? member.getSociety().getUnion().getCode() : null);
//        subLedger.setInitData();
//        subLedgerRepository.customSave(subLedger, identityInfo);
//        subLedgerRepository.save(subLedger);

//        List<SubLedgerLedgerConfig> listConfig = subLedgerLedgerConfigRepository.findBySubLedgerType(1);
//        if (listConfig != null && !listConfig.isEmpty()) {
//            for (Ledger ledger : listConfig.stream().map(m -> m.getLedger()).collect(Collectors.toList())) {
//                LedgerSubLedgerMapping mapping = new LedgerSubLedgerMapping();
//                mapping.setCode(subLedger.getCode() + "-" + ledger.getCode());
//                mapping.setSubLedger(subLedger);
//                mapping.setLedger(ledger);
//                mapping.setSociety(member.getSociety());
//                mapping.setUnionCode(member.getSociety().getUnion() != null ? member.getSociety().getUnion().getCode() : null);
//                mappingRepository.customSave(mapping, identityInfo);
////                mappingRepository.save(mapping);
//            }
//        }



        return memberDtoNew;
    }

    @Override
    public List<MemberImportDto> importMembers(List<MemberDto> dtoList, String header) {
        List<MemberImportDto> list = new ArrayList<>();
        dtoList.forEach(item -> {
            try {
                Optional<Member> memberData = repository.findById(item.getMember().getCode());
                if (memberData.isPresent()) {
                    Member memberOld = memberData.get();
                    memberOld.setFirstName(item.getMember().getFirstName());
                    memberOld.setMiddleName(item.getMember().getMiddleName());
                    memberOld.setLastName(item.getMember().getLastName());
                    memberOld.setFirstNameLocal(item.getMember().getFirstNameLocal());
                    memberOld.setMiddleNameLocal(item.getMember().getMiddleNameLocal());
                    memberOld.setLastNameLocal(item.getMember().getLastNameLocal());
//                    memberOld.setFirstName(!item.getMember().getFirstName().equalsIgnoreCase("") ? item.getMember().getFirstName() : memberOld.getFirstName());
//                    memberOld.setMiddleName(!item.getMember().getMiddleName().equalsIgnoreCase("") ? item.getMember().getMiddleName() : memberOld.getMiddleName());
//                    memberOld.setLastName(!item.getMember().getLastName().equalsIgnoreCase("") ? item.getMember().getLastName() : memberOld.getLastName());
//                    memberOld.setFirstNameLocal(!item.getMember().getFirstNameLocal().equalsIgnoreCase("") ? item.getMember().getFirstNameLocal() : memberOld.getFirstNameLocal());
//                    memberOld.setMiddleNameLocal(!item.getMember().getMiddleNameLocal().equalsIgnoreCase("") ? item.getMember().getMiddleNameLocal() : memberOld.getMiddleNameLocal());
//                    memberOld.setLastNameLocal(!item.getMember().getLastNameLocal().equalsIgnoreCase("") ? item.getMember().getLastNameLocal() : memberOld.getLastNameLocal());
                    memberOld.setMemberType(item.getMember().getMemberType());
                    memberOld.setMilkType(item.getMember().getMilkType());
                    memberOld.setMobileNo(!item.getMember().getMobileNo().equalsIgnoreCase("") ? item.getMember().getMobileNo() : memberOld.getMobileNo());
                    memberOld.setxCol1(item.getMember().getxCol1());
                    memberOld.setupdateData();
                    repository.customUpdate(memberOld, header);

                    MemberDetail memberDetailOld = memberDetailrepository.findById(item.getMemberDetail().getCode())
                            .get();
                    memberDetailOld.setGender(item.getMemberDetail().getGender());
                    memberDetailOld.setAccountNo(!item.getMemberDetail().getAccountNo().equalsIgnoreCase("") ? item.getMemberDetail().getAccountNo() : memberDetailOld.getAccountNo());
                    memberDetailOld.setIfsc(!item.getMemberDetail().getIfsc().equalsIgnoreCase("") ? item.getMemberDetail().getIfsc() : memberDetailOld.getIfsc());
                    memberDetailOld.setBank(item.getMemberDetail().getBank());
                    memberDetailOld.setPaymentMode(item.getMemberDetail().getPaymentMode());
                    memberDetailOld.setupdateData();

                    memberDetailrepository.customUpdate(memberDetailOld, header);

                    list.add(new MemberImportDto(item.getMember().getCode(), "update", "success"));
                } else {
                    Member member = item.getMember();
                    member.setInitData();



//                    SubLedger subLedger = new SubLedger();
//                    String code = nextCodeService.getNextCode("SubLedger", "code", member.getSociety().getCode(), 0);
//                    subLedger.setCode(code);
//                    subLedger.setReferenceCode(member.getCode());
//                    subLedger.setType(member.getMemberType().getCode() == 1 ? (short) 1 : (short) 2);
//                    subLedger.setName(member.getFirstName() + " " + member.getMiddleName() + " " + member.getLastName());
//                    subLedger.setNameLocal(member.getFirstNameLocal() + " " + member.getMiddleNameLocal() + " " + member.getLastNameLocal());
//                    subLedger.setSociety(member.getSociety());
//                    subLedger.setUnionCode(member.getSociety().getUnion() != null ? member.getSociety().getUnion().getCode() : null);
//                    subLedger.setInitData();
//                    subLedgerRepository.customSave(subLedger, header);
//        subLedgerRepository.save(subLedger);

//                    List<SubLedgerLedgerConfig> listConfig = subLedgerLedgerConfigRepository.findBySubLedgerType(1);
//                    if (listConfig != null && !listConfig.isEmpty()) {
//                        for (Ledger ledger : listConfig.stream().map(m -> m.getLedger()).collect(Collectors.toList())) {
//                            LedgerSubLedgerMapping mapping = new LedgerSubLedgerMapping();
//                            mapping.setCode(subLedger.getCode() + "-" + ledger.getCode());
//                            mapping.setSubLedger(subLedger);
//                            mapping.setLedger(ledger);
//                            mapping.setSociety(member.getSociety());
//                            mapping.setUnionCode(member.getSociety().getUnion() != null ? member.getSociety().getUnion().getCode() : null);
//                            mappingRepository.customSave(mapping, header);
////                mappingRepository.save(mapping);
//                        }
//                    }

                    Member memberNew = repository.customSave(member, header);

                    MemberDetail memberDetail = item.getMemberDetail();
                    memberDetail.setCode(member.getCode());
                    memberDetail.setInitData();
                    MemberDetail dtlNew = memberDetailrepository.customSave(memberDetail, header);

                    // Credit limit
//                    if (memberNew.getMemberType().getCode() == 1)
//                        setMemberCreditLimit(memberNew, dtlNew);
//                    setMemberCreditLimit(memberNew, dtlNew);
                    list.add(new MemberImportDto(item.getMember().getCode(), "insert", "success"));
                }
            } catch (Exception e) {
                list.add(new MemberImportDto(item.getMember().getCode(), e.getMessage(), "error"));
            }
        });
        return list;
    }

    private void setMemberCreditLimit(Member member, MemberDetail memberDetail) {
        String code = nextCodeRepository.getNextCode("MemberCreditLimit", "code", member.getSociety().getCode(), 0);
        MemberCreditLimit memberCreditLimit = new MemberCreditLimit();
        memberCreditLimit.setBalance(member.getCreditLimit());
        memberCreditLimit.setConsumerCode(member.getCode());
        memberCreditLimit.setConsumerType(CommonUtil.getMemberNonMemberTypeValue(member.getMemberType().getName()));
        memberCreditLimit.setSociety(member.getSociety());
        memberCreditLimit.setUnionCode(memberDetail.getUnionCode());
        memberCreditLimit.setInitData();
        memberCreditLimit.setCode(code);
        memberCreditLimitRepository.save(memberCreditLimit);

        code = nextCodeRepository.getNextCode("MemberCreditLimitTransaction", "code", memberCreditLimit.getCode() + "T",
                0);
        MemberCreditLimitTransaction memberCreditLimitTransaction = new MemberCreditLimitTransaction();
        memberCreditLimitTransaction.setCode(code);
        memberCreditLimitTransaction.setBalance(memberCreditLimit.getBalance());
        memberCreditLimitTransaction.setNewValue(member.getCreditLimit());
        memberCreditLimitTransaction.setOldValue(BigDecimal.valueOf(0));
        memberCreditLimitTransaction.setConsumerCode(memberCreditLimit.getConsumerCode());
        memberCreditLimitTransaction
                .setConsumerType(CommonUtil.getMemberNonMemberTypeValue(member.getMemberType().getName()));
        memberCreditLimitTransaction.setReferenceCode(member.getCode());
        memberCreditLimitTransaction.setSociety(memberCreditLimit.getSociety());
        memberCreditLimitTransaction.setTransactionType("Initial credit limit");
        memberCreditLimitTransaction.setTransactionDate(LocalDate.now());
        memberCreditLimitTransaction.setUnionCode(memberCreditLimit.getUnionCode());
        memberCreditLimitTransaction.setInitData();
        memberCreditLimitTxnRepository.save(memberCreditLimitTransaction);
    }

    @Override
    @Transactional
    public MemberDto update(MemberDto memberDto, String identityInfo) {
        MemberDto dtoNew = new MemberDto();
        Optional<Member> old = repository.findById(memberDto.getMember().getCode());

        Member member = memberDto.getMember();
        member.setupdateData();
        dtoNew.setMember(repository.customUpdate(member, identityInfo));

        MemberDetail memberDetail = memberDto.getMemberDetail();
        memberDetail.setupdateData();
        dtoNew.setMemberDetail(memberDetailrepository.customUpdate(memberDetail, identityInfo));



//        Optional<SubLedger> sl  = subLedgerRepository.findByReferenceCodeAndType(member.getCode(),old.get().getMemberType().getCode().shortValue());
//        if(sl.isPresent()) {
//            sl.get().setName(member.getFirstName() + " " + member.getMiddleName() + " " + member.getLastName());
//            sl.get().setType(member.getMemberType().getCode() == 1 ? (short) 1 : (short) 2);
//            sl.get().setNameLocal(member.getFirstNameLocal() + " " + member.getMiddleNameLocal() + " " + member.getLastNameLocal());
//            sl.get().setupdateData();
//            subLedgerRepository.customUpdate(sl.get(), identityInfo);
//        }



        return dtoNew;
    }

    @Override
    public Optional<Member> findById(String code) {
        return repository.findById(code);
    }

    @Override
    public MemberDetail findDetailByMemberCode(String code) {
        Member member = repository.findById(code)
                .orElseThrow(() -> new EntityNotFoundException(Member.class, "invalid.membercode"));
        MemberDetail dtl = memberDetailrepository.findByMember(member)
                .orElseThrow(() -> new EntityNotFoundException(MemberDetail.class, "invalid.membercode"));
        return dtl;
    }

    @Override
    @Transactional
    public void delete(String code, String identityInfo) {
        Member member = repository.findById(code)
                .orElseThrow(() -> new EntityNotFoundException(Member.class, "invalid.membercode"));
        MemberDetail detail = memberDetailrepository.findByMember(member)
                .orElseThrow(() -> new EntityNotFoundException(MemberDetail.class, "invalid.membercode"));
        detail.setState(Hibernate.unproxy(detail.getState(), State.class));
        detail.setDistrict(Hibernate.unproxy(detail.getDistrict(), District.class));
        detail.setSubDistrict(Hibernate.unproxy(detail.getSubDistrict(), SubDistrict.class));
        detail.setVillage(Hibernate.unproxy(detail.getVillage(), Village.class));
        detail.setHamlet(Hibernate.unproxy(detail.getHamlet(), Hamlet.class));
        detail.setBank(Hibernate.unproxy(detail.getBank(), Bank.class));
        detail.setBranch(Hibernate.unproxy(detail.getBranch(), Branch.class));
        memberDetailrepository.customDelete(detail, identityInfo);
        repository.customDelete(member, identityInfo);
    }

    @Override
    public MemberSocietyInfoDto findMemberInformation(String code, LocalDateTime date, Integer count, String paymentCycle) {
        Member member = repository.findById(code).orElseThrow(() -> new BusinessValidationFailException(Member.class,
                CommonUtil.getFieldError("member", "code", code, "member.notfound")));
        MemberSocietyInfoDto dto = new MemberSocietyInfoDto();

        List<MilkCollection> collections = collectionRepository
                .findByMemberAndCollectionDateLessThanEqualOrderByCollectionDateDesc(member, date, Pageable.ofSize(count));
        if (paymentCycle != null && !paymentCycle.isEmpty()) {
            Optional<SocietyPaymentCycle> op = paymentCycleRepository.findById(paymentCycle);
            List<MilkCollection> currentPaymentCycleData = op.isPresent() ?
                    collectionRepository.findByMemberAndSocietyPaymentCycle(member, op.get()) : null;
            for (MilkCollection currentPaymentCycleDatum : currentPaymentCycleData) {
                currentPaymentCycleDatum.setShift(Hibernate.unproxy(currentPaymentCycleDatum.getShift(), Shift.class));
            }
            dto.setCurrentPaymentCycleData(currentPaymentCycleData);
        }
        dto.setMember(member);
        dto.setMemberCollection(collections.stream().filter(p -> p.getCollectionDate().compareTo(date) == 0)
                .collect(Collectors.toList()));
        dto.setPrevCollectionData(collections);
        return dto;
    }

    @Override
    public Member findByMemberCode(String code) {
        Optional<Member> member = repository.findById(code);
        if (member.isPresent()) {
            member.get().setSociety(Hibernate.unproxy(member.get().getSociety(), Society.class));
            return member.get();
        } else
            return null;
    }

    @Override
    @Transactional(readOnly = true)
    public MemberDetail findDetailByMember(Member member) {
        MemberDetail dtl = memberDetailrepository.findByMember(member).get();
        dtl.setMember(Hibernate.unproxy(dtl.getMember(), Member.class));
        dtl.setState(Hibernate.unproxy(dtl.getState(), State.class));
        dtl.setDistrict(Hibernate.unproxy(dtl.getDistrict(), District.class));
        dtl.setVillage(Hibernate.unproxy(dtl.getVillage(), Village.class));
        dtl.setSubDistrict(Hibernate.unproxy(dtl.getSubDistrict(), SubDistrict.class));
        dtl.setHamlet(Hibernate.unproxy(dtl.getHamlet(), Hamlet.class));
        dtl.setBank(Hibernate.unproxy(dtl.getBank(), Bank.class));
        dtl.setBranch(Hibernate.unproxy(dtl.getBranch(), Branch.class));
        return dtl;
    }

    @Override
    public MemberRegister findMemberRegisterData(String societyCode, LocalDateTime dt, String memberType) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<MemberDetail> findAllMemberDetails() {
        List<MemberDetail> list = memberDetailrepository.findAll();
        for (MemberDetail memberDetail : list) {
            memberDetail.setMember(Hibernate.unproxy(memberDetail.getMember(), Member.class));
        }
        return list;
    }

}