package com.eipl.amcs.master.account.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.dto.VoucherDto;
import com.eipl.amcs.master.account.service.VoucherService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class VoucherLoadTask extends Task<List<VoucherDto>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(VoucherLoadTask.class);

    @Override
    protected List<VoucherDto> call() throws Exception {
        try {
            VoucherService service = EmcsAppContext.getContext().getBean(VoucherService.class);
            List<VoucherDto> list = service.findAll();
            if (list == null || list.isEmpty())
                return null;
            return list;
        } catch (Exception e) {
            LOGGER.error("Voucher fetch", e);
        }
        return null;
    }
}
