package com.eipl.amcs.master.procurement.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.procurement.model.SocietyPaymentCycle;
import com.eipl.amcs.master.procurement.service.SocietyPaymentCycleService;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

public class SocietyPaymentCycleLoadTask extends Task<List<SocietyPaymentCycle>> {

    @Override
    protected List<SocietyPaymentCycle> call() throws Exception {
        try {
            SocietyPaymentCycleService service = EmcsAppContext.getContext().getBean(SocietyPaymentCycleService.class);
            List<SocietyPaymentCycle> list = service.findAll();
            if (list == null || list.isEmpty())
                return null;
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

