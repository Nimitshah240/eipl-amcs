package com.eipl.amcs.master.account.service;

import com.eipl.amcs.base.service.NextCodeService;
import com.eipl.amcs.master.account.dto.YearClosingDto;
import com.eipl.amcs.master.account.model.*;
import com.eipl.amcs.master.account.repository.FinancialYearRepository;
import com.eipl.amcs.master.account.repository.LedgerOpeningBalanceRepository;
import com.eipl.amcs.master.account.repository.SocietyYearClosingRepository;
import com.eipl.amcs.master.account.repository.SubLedgerOpeningBalanceRepository;
import com.eipl.amcs.master.org.model.Society;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.eipl.amcs.MainApp.context;
import static com.eipl.amcs.config.BeanConfig.*;

@Service
public class FinancialYearServiceImpl implements FinancialYearService {

//	private FinancialYearRepository financialYearRepository;
//	private LedgerOpeningBalanceRepository ledgerOpeningBalanceRepository;
//	private SubLedgerOpeningBalanceRepository subLedgerOpeningBalanceRepository;
//	private SocietyYearClosingRepository closingRepository;
//	private NextCodeService nextCodeService;


	@Override
	public List<FinancialYear> findAll() {
		return financialYearRepository.findAll(Sort.by("startDate"));
	}

	@Override
	public YearClosingDto saveDto(YearClosingDto dto, String identityHeader) {
		List<LedgerOpeningBalance> ledgerOpeningBalanceList = dto.getLedgerOpeningBalanceList();
		List<SubLedgerOpeningBalance> subLedgerOpeningBalanceList = dto.getSubLedgerOpeningBalanceList();
		SocietyYearClosing closing = dto.getSocietyYearClosing();
		importLedgerBalance(ledgerOpeningBalanceList,identityHeader);
		importSubLedgerBalance(subLedgerOpeningBalanceList,identityHeader);
		saveClosing(closing,identityHeader);

		return null;
	}

	public List<LedgerOpeningBalance> importLedgerBalance(List<LedgerOpeningBalance> ledgerList, String header) {
		List<LedgerOpeningBalance> list = new ArrayList<>();
		ledgerList.forEach(item -> {
			LedgerOpeningBalance led = null;
			try {
				if (item.getCode() != null) {
					Optional<LedgerOpeningBalance> ledBalanceData = ledgerOpeningBalanceRepository.findById(item.getCode());
					if (ledBalanceData.isPresent()) {
						LedgerOpeningBalance ledBal = ledBalanceData.get();
						ledBal.setLedger(item.getLedger());
						ledBal.setFinancialYearsCode(item.getFinancialYearsCode());
						ledBal.setCreditDebit(item.getCreditDebit());
						ledBal.setupdateData();
						ledgerOpeningBalanceRepository.customUpdate(ledBal, header);
						list.add(ledBal);
					}
				} else {
					String code = nextCodeService.getNextCode("LedgerOpeningBalance", "code", item.getSociety().getCode(), 0);
					item.setCode(code);
					item.setInitData();
					item.setSociety(Hibernate.unproxy(item.getSociety(), Society.class));
					led = ledgerOpeningBalanceRepository.customSave(item, header);
					list.add(led);
				}
			} catch (Exception e) {
				list.add(led);
			}
		});

		return null;
	}



	public List<SubLedgerOpeningBalance> importSubLedgerBalance(List<SubLedgerOpeningBalance> subledgerbList, String header) {
		List<SubLedgerOpeningBalance> list = new ArrayList<>();

		subledgerbList.forEach(item -> {
			SubLedgerOpeningBalance subled = null;
			try {
				if (item.getCode() != null) {
					Optional<SubLedgerOpeningBalance> slBalanceData = subLedgerOpeningBalanceRepository.findById(item.getCode());
					if (slBalanceData.isPresent()) {
						SubLedgerOpeningBalance subledBal = slBalanceData.get();
						subledBal.setSubLedger(item.getSubLedger());
						subledBal.setSociety(item.getSociety());
						subledBal.setUnionCode(item.getUnionCode());
						subledBal.setFinancialYearsCode(item.getFinancialYearsCode());
						subledBal.setCreditDebit(item.getCreditDebit());

						subledBal.setupdateData();
						subLedgerOpeningBalanceRepository.customUpdate(subledBal, header);
						list.add(subledBal);
					}
				} else {
					String code = nextCodeService.getNextCode("SubLedgerOpeningBalance", "code", item.getSociety().getCode(), 0);
					item.setCode(code);
					item.setSociety(Hibernate.unproxy(item.getSociety(), Society.class));
					item.setInitData();
					subled = subLedgerOpeningBalanceRepository.customSave(item, header);
					list.add(subled);
				}
			} catch (Exception e) {
				list.add(subled);
			}
		});
		for (SubLedgerOpeningBalance subLedgerOpeningBalance : list) {
			subLedgerOpeningBalance.setSubLedger(Hibernate.unproxy(subLedgerOpeningBalance.getSubLedger(), SubLedger.class));
			subLedgerOpeningBalance.setSociety(Hibernate.unproxy(subLedgerOpeningBalance.getSociety(),Society.class));
			subLedgerOpeningBalance.setLedger(Hibernate.unproxy(subLedgerOpeningBalance.getLedger(),Ledger.class));
		}
		return list;
	}


	public SocietyYearClosing saveClosing(SocietyYearClosing societyYearClosing, String identityInfo) {
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