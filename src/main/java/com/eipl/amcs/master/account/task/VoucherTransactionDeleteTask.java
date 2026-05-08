package com.eipl.amcs.master.account.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.repository.VoucherTransactionRepository;
import javafx.concurrent.Task;

public class VoucherTransactionDeleteTask extends Task<Boolean> {
    private final String code;

    public VoucherTransactionDeleteTask(String code) {
        this.code = code;
    }

    @Override
    protected Boolean call() throws Exception {
        try {
            VoucherTransactionRepository repository = EmcsAppContext.getContext().getBean(VoucherTransactionRepository.class);
            repository.deleteById(this.code);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
