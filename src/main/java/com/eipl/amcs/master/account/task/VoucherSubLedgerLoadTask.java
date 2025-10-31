package com.eipl.amcs.master.account.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.model.VoucherSubLedger;
import com.eipl.amcs.master.account.model.VoucherTransaction;
import com.eipl.amcs.master.account.repository.VoucherTransactionRepository;
import com.eipl.amcs.master.account.service.VoucherService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class VoucherSubLedgerLoadTask extends Task<List<VoucherSubLedger>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(VoucherSubLedgerLoadTask.class);

    private final String code;

    public VoucherSubLedgerLoadTask(String code) {
        this.code = code;
    }

    @Override
    protected List<VoucherSubLedger> call() throws Exception {
        try {
            VoucherService service = EmcsAppContext.getContext().getBean(VoucherService.class);
            VoucherTransactionRepository transactionRepository = EmcsAppContext.getContext().getBean(VoucherTransactionRepository.class);
            VoucherTransaction voucherTransaction = transactionRepository.findById(code).get();
            List<VoucherSubLedger> list = service.findAllVoucherSubLedger(voucherTransaction);
            if (list == null || list.isEmpty())
                return null;
            return list;
        } catch (Exception e) {
            LOGGER.error("List<VoucherSubLedger> fetch", e);
        }
        return null;
    }
}
