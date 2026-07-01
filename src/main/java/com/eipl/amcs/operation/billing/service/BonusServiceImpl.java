package com.eipl.amcs.operation.billing.service;

import com.eipl.amcs.base.service.NextCodeService;
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.master.operation.model.MemberDetail;
import com.eipl.amcs.master.operation.repository.MemberDetailRepository;
import com.eipl.amcs.master.operation.repository.MemberRepository;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.org.model.Union;
import com.eipl.amcs.operation.billing.dto.BonusDto;
import com.eipl.amcs.operation.billing.model.Bonus;
import com.eipl.amcs.operation.billing.model.BonusSummary;
import com.eipl.amcs.operation.billing.repository.BonusRepository;
import com.eipl.amcs.operation.billing.repository.BonusSummaryRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
public class BonusServiceImpl implements BonusService {

    @Autowired
    BonusRepository bonusRepository;
    @Autowired
    BonusSummaryRepository bonusSummaryRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    MemberDetailRepository memberDetailRepository;

    @Autowired
    NextCodeService nextCodeService;

    @Override
    public List<BonusSummary> findBonusSummaryBetWeen() {
        // TODO Auto-generated method stub
        return bonusSummaryRepository.findAll();
    }


    @Override
    @Transactional
    public List<Bonus> loadData(LocalDateTime fromDate, LocalDateTime toDate, Integer milkType) {
        List<Map<String, Object>> a = new ArrayList<>();
        // TODO Auto-generated method stub
        if (milkType != 0)
            a = bonusRepository.loadData(fromDate, toDate, milkType);
        else
            a = bonusRepository.loadData(fromDate, toDate);
        List<Bonus> list = new ArrayList();
        for (Map<String, Object> map : a) {
            Bonus b = new Bonus();
            b.setMilkAmount((BigDecimal) map.get("amt"));
            b.setMilkQty((BigDecimal) map.get("qty"));
            Member m = memberRepository.findByCode((String) map.get("member_code"));
            MemberDetail md = memberDetailRepository.findByMember(m).orElse(null);
            b.setMember(m);
            b.setMemberName(m.getFirstName());
            if (md != null) {
                b.setBank(md.getBank());
                b.setBankName(md.getBank().toString());
                b.setAccountNo(md.getAccountNo());
                b.setIfsc(md.getIfsc());
            }
            b.setStatus((short) 0);
            list.add(b);
        }
        return list;
    }

    @Override
    public Map<String, Object> loadDataBonus(LocalDate fromDate, LocalDate toDate, String memberCode) {
        Map<String, Object> map = bonusRepository.loadDataBonus(fromDate, toDate, memberCode);
        return map;
    }

    @Override
    public List<Map<String, Object>> loadDataBonusSummary(LocalDate fromDate, LocalDate toDate) {
        List<Map<String, Object>> map = bonusRepository.loadDataBonusSummary(fromDate, toDate);
        return map;
    }


    @Override
    public BonusDto updateDto(String identityInfo, BonusDto dto) {
        List<Bonus> list = dto.getBonusList();
        BonusSummary bs = dto.getBonusSummary();
        bs.setupdateData();
        bonusSummaryRepository.customUpdate(bs, identityInfo);
        for (Bonus b : list) {
            b.setupdateData();
            bonusRepository.customUpdate(b, identityInfo);
        }
        return dto;
    }

    @Override
    public BonusDto editDto(String identityInfo, BonusDto dto) {
        saveDto(identityInfo, dto, (short) 1);
        return null;
    }


    @Override
    public BonusSummary save(String identityInfo, BonusSummary bonusSummary) {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    @Transactional
    public BonusDto saveDto(String identityInfo, BonusDto dto, short s) {
        if (s == 0) {
            List<Bonus> list = dto.getBonusList();
            BonusSummary bs = dto.getBonusSummary();
            String code = nextCodeService.getNextCode("BonusSummary", "code", bs.getSociety().getCode(), 0);
            bs.setType(list.get(0).getType());
            bs.setInitData();
            bs.setCode(code);
            bonusSummaryRepository.customSave(bs, identityInfo);
            int a = 1;
            for (Bonus b : list) {
                b.setCode(code + a);
                a++;
                b.setBonusSummary(bs);
                b.setInitData();
                bonusRepository.customSave(b, identityInfo);
            }
            return dto;
        } else {
            List<Bonus> list = dto.getBonusList();
            BonusSummary bs = dto.getBonusSummary();
            bs.setType(list.get(0).getType());
            bs.setupdateData();
            String code = bs.getCode();
            bonusSummaryRepository.customUpdate(bs, identityInfo);
            int a = 1;
            for (Bonus b : list) {
                b.setCode(code + a);
                a++;
                b.setBonusSummary(bs);
                b.setupdateData();
                bonusRepository.customUpdate(b, identityInfo);
            }
            return dto;
        }
    }


    @Override
    public BonusDto findBySummary(String code) {
        // TODO Auto-generated method stub
        BonusSummary bonusSummary = bonusSummaryRepository.findById(code).get();
        bonusSummary.setSociety(Hibernate.unproxy(bonusSummary.getSociety(), Society.class));
        bonusSummary.setUnion(Hibernate.unproxy(bonusSummary.getUnion(), Union.class));
        List<Bonus> list = bonusRepository.findByBonusSummary(bonusSummary);
        list.forEach(item -> {
            item.setSociety(Hibernate.unproxy(item.getSociety(), Society.class));
            item.setUnion(Hibernate.unproxy(item.getUnion(), Union.class));
            item.setBonusSummary(bonusSummary);
        });
        BonusDto dto = new BonusDto();
        dto.setBonusList(list);
        dto.setBonusSummary(bonusSummary);
        return dto;
    }


    @Override
    public Boolean deleteDto(String identityInfo, String code) {
        BonusSummary bonusSummary = bonusSummaryRepository.findById(code).get();
        List<Bonus> b = bonusRepository.findByBonusSummary(bonusSummary);
        for (Bonus bonus : b) {
            bonusRepository.customDelete(bonus, identityInfo);
        }
        bonusSummaryRepository.customDelete(bonusSummary, identityInfo);
        return true;
    }
}
