package com.eipl.amcs.master.account.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.dto.VoucherTypeMappingDto;
import com.eipl.amcs.master.account.model.Ledger;
import com.eipl.amcs.master.account.model.VoucherType;
import com.eipl.amcs.master.account.model.VoucherTypeLedgerConfig;
import com.eipl.amcs.master.account.service.LedgerService;
import com.eipl.amcs.master.account.service.VoucherTypeLedgerConfigService;
import com.eipl.amcs.master.account.service.VoucherTypeService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class VoucherTypeLedgerConfigLoadTask extends Task<VoucherTypeMappingDto> {
    private static final Logger LOGGER = LoggerFactory.getLogger(VoucherTypeLedgerConfigLoadTask.class);

    @Override
    protected VoucherTypeMappingDto call() throws Exception {
        try {
            VoucherTypeService voucherTypeService = EmcsAppContext.getContext().getBean(VoucherTypeService.class);
            LedgerService ledgerService = EmcsAppContext.getContext().getBean(LedgerService.class);
            VoucherTypeLedgerConfigService voucherTypeLedgerConfigService = EmcsAppContext.getContext().getBean(VoucherTypeLedgerConfigService.class);

            List<VoucherType> voucherTypeList = new ArrayList<>(voucherTypeService.findAll());

            // ledger
            List<Ledger> ledgerList = ledgerService.findAllByIsActive();

            List<VoucherTypeLedgerConfig> mapping = voucherTypeLedgerConfigService.findAll();

            List<VoucherTypeLedgerConfig> listMapping = new ArrayList<>(mapping);
            for (VoucherTypeLedgerConfig mp : listMapping) {
                voucherTypeList.removeIf(p -> p.getCode().toString().equalsIgnoreCase(mp.getVoucherType().getCode().toString()));
            }
            for (VoucherType voucherType : voucherTypeList) {
                VoucherTypeLedgerConfig mp = new VoucherTypeLedgerConfig();
                mp.setVoucherType(voucherType);
                listMapping.add(mp);
            }

            List<Ledger> list = new ArrayList<>(ledgerList);
            list.add(0, new Ledger("None"));//"0",
            return new VoucherTypeMappingDto(listMapping, list);
        } catch (Exception e) {
            LOGGER.error("VoucherTypeLedgerConfig fetch", e);
        }
        return null;
    }
}
