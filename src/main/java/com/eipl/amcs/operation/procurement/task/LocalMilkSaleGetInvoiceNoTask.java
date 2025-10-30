package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.service.NextCodeService;
import com.eipl.amcs.config.EmcsAppContext;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LocalMilkSaleGetInvoiceNoTask extends Task<String> {
    private static final Logger LOGGER = LoggerFactory.getLogger(LocalMilkSaleGetInvoiceNoTask.class);

    public LocalMilkSaleGetInvoiceNoTask() {
    }

    @Override
    protected String call() throws Exception {
        try {
            String code = MainApp.identityDto.getSociety().getCode() + "/" + MainApp.getFinancialYear().getCode() + "/";
            NextCodeService nextCodeService = EmcsAppContext.getContext().getBean(NextCodeService.class);
            String codes = nextCodeService.getNextCode("LocalMilkSale", "invoiceNo", code, 5);
            if (codes == null || codes.isEmpty())
                return null;
            return codes;
        } catch (Exception e) {
            LOGGER.error("LocalMilkSale InvoiceNo fetched: {}", e);
        }
        return null;
    }

}
