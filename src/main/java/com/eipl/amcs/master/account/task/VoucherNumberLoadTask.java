package com.eipl.amcs.master.account.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.service.NextCodeService;
import com.eipl.amcs.config.EmcsAppContext;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VoucherNumberLoadTask extends Task<String> {
    private static final Logger LOGGER = LoggerFactory.getLogger(VoucherNumberLoadTask.class);

    public VoucherNumberLoadTask() {
    }

    @Override
    protected String call() throws Exception {
        try {
            String code = MainApp.identityDto.getSociety().getCode() + "/" + MainApp.getFinancialYear().getCode() + "/";
            NextCodeService nextCodeService = EmcsAppContext.getContext().getBean(NextCodeService.class);
            String codeI = nextCodeService.getNextCode("Voucher", "code", code, 0);
            if (codeI == null || codeI.isBlank())
                return null;
            return codeI;
        } catch (Exception e) {
            LOGGER.error("ProductSale No fetch", e);
        }
        return null;
    }

}
