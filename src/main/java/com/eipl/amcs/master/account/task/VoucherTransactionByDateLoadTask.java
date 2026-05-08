package com.eipl.amcs.master.account.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.dto.VoucherDto;
import com.eipl.amcs.master.account.model.VoucherTransaction;
import com.eipl.amcs.master.account.service.VoucherService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;

public class VoucherTransactionByDateLoadTask extends Task<List<VoucherTransaction>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(VoucherTransactionByDateLoadTask.class);

    private LocalDate fromDate;
    private LocalDate toDate;

    public VoucherTransactionByDateLoadTask(LocalDate fromDate, LocalDate toDate) {
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    @Override
    protected List<VoucherTransaction> call() throws Exception {
        try {
            VoucherService service = EmcsAppContext.getContext().getBean(VoucherService.class);
            List<VoucherTransaction> list = service.loadVoucherByVoucherDateBetween(fromDate, toDate);
            if (list == null || list.isEmpty())
                return null;
            return list;
        } catch (Exception e) {
            LOGGER.error("Voucher fetch", e);
        }
        return null;
    }
}
