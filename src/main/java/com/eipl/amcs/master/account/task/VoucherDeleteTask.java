package com.eipl.amcs.master.account.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.model.Voucher;
import com.eipl.amcs.master.account.repository.VoucherRepository;
import com.eipl.amcs.master.account.service.VoucherService;
import com.eipl.amcs.utils.CommonUtils;
import javafx.concurrent.Task;

import java.util.Optional;

public class VoucherDeleteTask extends Task<Boolean> {
    private final String code;

    public VoucherDeleteTask(String code) {
        this.code = code;
    }

    @Override
    protected Boolean call() throws Exception {
        try {
            VoucherService service = EmcsAppContext.getContext().getBean(VoucherService.class);
            VoucherRepository repository = EmcsAppContext.getContext().getBean(VoucherRepository.class);
            Optional<Voucher> voucher = repository.findById(code);
            service.delete(voucher.get(), CommonUtils.setIdentityHeader());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
