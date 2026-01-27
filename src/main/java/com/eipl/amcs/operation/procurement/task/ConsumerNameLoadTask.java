package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.operation.service.CustomerService;
import com.eipl.amcs.master.operation.service.MemberService;
import javafx.concurrent.Task;

public class ConsumerNameLoadTask extends Task<String> {
    private final Integer type;
    private final String code;

    public ConsumerNameLoadTask(Integer type, String code) {
        this.type = type;
        this.code = code;
    }

    @Override
    protected String call() throws Exception {
        if (type == null || code == null) return "";

        if (type == 1 || type == 2) {
            MemberService memberService = EmcsAppContext.getContext().getBean(MemberService.class);
            var m = memberService.findByMemberCode(code);
            return (m != null) ? m.toMemberName() : code;
        } else {
            CustomerService customerService = EmcsAppContext.getContext().getBean(CustomerService.class);
            var c = customerService.findByCustomerCode(code);
            return (c != null) ? c.getName() : code;
        }
    }
}
