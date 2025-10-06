package com.eipl.amcs.master.account.service;

import com.eipl.amcs.base.service.NextCodeService;
import com.eipl.amcs.master.account.model.FinancialYear;
import com.eipl.amcs.master.account.model.SocietyYearClosing;
import com.eipl.amcs.master.account.repository.SocietyYearClosingRepository;
import com.eipl.amcs.master.org.model.Society;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class SocietyYearClosingServiceImpl implements SocietyYearClosingService {

    @Autowired
    private SocietyYearClosingRepository closingRepository;
    @Autowired
    private NextCodeService nextCodeService;

    private static final Logger log = LoggerFactory.getLogger(SocietyYearClosingServiceImpl.class);

    @Override
    public List<SocietyYearClosing> findAll() {
        List<SocietyYearClosing> list = closingRepository.findAll(Sort.by("name"));

        log.info("BasicTaxes findAll {} items fetched", list.size());
        return list;
    }

    @Override
    public SocietyYearClosing save(SocietyYearClosing societyYearClosing, String identityInfo) {
        String code = nextCodeService.getNextCode("SocietyYearClosing", "society_year_closing_code", societyYearClosing.getSociety().getCode(), 0);
        societyYearClosing.setCode(code);
        societyYearClosing.setInitData();
        societyYearClosing.setSociety(Hibernate.unproxy(societyYearClosing.getSociety(), Society.class));
        societyYearClosing.setFinancialYear(Hibernate.unproxy(societyYearClosing.getFinancialYear(), FinancialYear.class));
        SocietyYearClosing obj =  closingRepository.save(societyYearClosing);
        obj.setSociety(Hibernate.unproxy(obj.getSociety(), Society.class));
        obj.setFinancialYear(Hibernate.unproxy(obj.getFinancialYear(), FinancialYear.class));
        return obj;
    }
}