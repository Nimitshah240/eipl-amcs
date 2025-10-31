package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.operation.service.MemberService;
import com.eipl.amcs.utils.ApiJsonUtil;
import javafx.concurrent.Task;
import org.springframework.web.client.HttpStatusCodeException;

import java.time.LocalDateTime;

public class MemberSocietyInfoLoadTask extends Task<Object> {
    private final String code;
    private final LocalDateTime date;
    private Integer count;
    private String paymentCycleCode;

    public MemberSocietyInfoLoadTask(String code, LocalDateTime date) {
        this.code = code;
        this.date = date;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public void setPaymentCycleCode(String paymentCycleCode) {
        this.paymentCycleCode = paymentCycleCode;
    }

    @Override
    protected Object call() throws Exception {
        try {
            MemberService service = EmcsAppContext.getContext().getBean(MemberService.class);
            if (count == null)
                count = 5;

            return service.findMemberInformation(code, date, count, paymentCycleCode);
        } catch (HttpStatusCodeException e) {
            return EmcsAppContext.getContext().getBean(ApiJsonUtil.class).parseJsonString(e.getResponseBodyAsString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }
}
