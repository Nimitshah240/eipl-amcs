package com.eipl.amcs.master.account.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.dto.TaxDetailMappingDto;
import com.eipl.amcs.master.account.dto.TaxDto;
import com.eipl.amcs.master.account.model.Ledger;
import com.eipl.amcs.master.account.model.LedgerMappingTaxDetail;
import com.eipl.amcs.master.account.model.Tax;
import com.eipl.amcs.master.account.model.TaxDetail;
import com.eipl.amcs.master.account.service.LedgerMappingTaxDetailService;
import com.eipl.amcs.master.account.service.LedgerService;
import com.eipl.amcs.master.account.service.TaxService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class LedgerMappingTaxDetailLoadTask extends Task<TaxDetailMappingDto> {
    private static final Logger LOGGER = LoggerFactory.getLogger(LedgerMappingTaxDetailLoadTask.class);

    @Override
    protected TaxDetailMappingDto call() throws Exception {
        try {
            //Tax
            TaxService taxService = EmcsAppContext.getContext().getBean(TaxService.class);
            List<TaxDto> taxDetailList = taxService.findAll();

            // ledger
            LedgerService ledgerService = EmcsAppContext.getContext().getBean(LedgerService.class);
            List<Ledger> ledgerList = ledgerService.findAllByIsActive();

            // mapping
            LedgerMappingTaxDetailService ledgerMappingTaxDetailService = EmcsAppContext.getContext().getBean(LedgerMappingTaxDetailService.class);
            List<LedgerMappingTaxDetail> mapping = ledgerMappingTaxDetailService.findAll();


            List<LedgerMappingTaxDetail> listMapping = new ArrayList<>(mapping);

            for (TaxDto taxDto : taxDetailList) {
                for (TaxDetail taxDetail : taxDto.getTaxDetails()) {
                    LedgerMappingTaxDetail obj = listMapping.stream().filter(p -> p.getTaxDetail().getCode().equalsIgnoreCase(taxDetail.getCode()))
                            .findFirst().orElse(null);

                    if (obj == null) {
                        LedgerMappingTaxDetail mp = new LedgerMappingTaxDetail();
                        mp.setTaxDetail(taxDetail);
                        mp.getTaxDetail().setTax(getTax(taxDetailList, taxDetail.getCode()));
                        listMapping.add(mp);
                    } else {
                        obj.setTaxDetail(taxDetail);
                        obj.getTaxDetail().setTax(getTax(taxDetailList, taxDetail.getCode()));
                    }
                }
            }
            List<Ledger> list = new ArrayList<>(ledgerList);
            list.add(0, new Ledger("None")); //"0",
            return new TaxDetailMappingDto(listMapping, list);
        } catch (Exception e) {
            LOGGER.error("LedgerMappingTaxDetail fetch", e);
        }
        return null;
    }

    private Tax getTax(List<TaxDto> taxDetailList, String code) {
        for (TaxDto taxDto : taxDetailList) {
            for (TaxDetail taxDetail : taxDto.getTaxDetails()) {
                if (taxDetail.getCode().equalsIgnoreCase(code))
                    return taxDto.getTax();
            }
        }
        return null;
    }
}
