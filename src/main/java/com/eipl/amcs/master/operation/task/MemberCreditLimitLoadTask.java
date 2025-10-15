package com.eipl.amcs.master.operation.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.operation.model.MemberCreditLimit;
import com.eipl.amcs.master.operation.service.MemberCreditLimitService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MemberCreditLimitLoadTask extends Task<MemberCreditLimit> {
    private static final Logger LOGGER = LoggerFactory.getLogger(MemberCreditLimitLoadTask.class);

    private final String code;
    private final Short type;

    public MemberCreditLimitLoadTask(String code, Short type) {
        this.code = code;
        this.type = type;
    }

    @Override
    protected MemberCreditLimit call() throws Exception {
        try {
            MemberCreditLimitService service = EmcsAppContext.getContext().getBean(MemberCreditLimitService.class);
            return service.findByConsumerCodeAndConsumerType(code, type).orElse(null);
        } catch (Exception e) {
            LOGGER.error("MemberCreditLimit fetch", e);
        }
        return null;
    }
}
