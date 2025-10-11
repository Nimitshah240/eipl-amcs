package com.eipl.amcs.master.global.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.service.SubLedgerService;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.global.service.MemberTypeService;
import com.eipl.amcs.master.global.service.MilkTypeService;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import static com.eipl.amcs.MainApp.context;

public class MilkTypeLoadTask extends Task<List<MilkType>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(MilkTypeLoadTask.class);

    @Override
    protected List<MilkType> call() throws Exception {
        try {
            MilkTypeService service = EmcsAppContext.getContext().getBean(MilkTypeService.class);
            List<MilkType> list = service.findAll();
            return list;
        } catch (Exception e) {
            LOGGER.error("MilkTypes fetch", e);
        }
        return null;
    }
}

