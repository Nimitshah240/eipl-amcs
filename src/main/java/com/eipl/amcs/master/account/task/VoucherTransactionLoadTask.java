package com.eipl.amcs.master.account.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.model.Voucher;
import com.eipl.amcs.master.account.model.VoucherTransaction;
import com.eipl.amcs.master.account.repository.VoucherRepository;
import com.eipl.amcs.master.account.service.VoucherService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class VoucherTransactionLoadTask extends Task<List<VoucherTransaction>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(VoucherTransactionLoadTask.class);

    private final String code;

    public VoucherTransactionLoadTask(String code) {
        this.code = code;
    }

    @Override
    protected List<VoucherTransaction> call() throws Exception {
        try {
            VoucherRepository repository = EmcsAppContext.getContext().getBean(VoucherRepository.class);
            VoucherService service = EmcsAppContext.getContext().getBean(VoucherService.class);
            Optional<Voucher> voucher = null;
            if (code != null) {
                 voucher = repository.findById(code);
            }
            if (voucher != null && voucher.isPresent()) {
                List<VoucherTransaction> list = service.findAllTransaction(voucher.get());
                if (list == null || list.isEmpty())
                    return null;
                return list;
            }
        } catch (Exception e) {
            LOGGER.error("List<VoucherTransaction> fetch", e);
        }
        return null;

    }
}