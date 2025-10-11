package com.eipl.amcs.master.global.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.global.model.MemberType;
import com.eipl.amcs.master.global.service.GenderService;
import com.eipl.amcs.master.global.service.MemberTypeService;
import com.eipl.amcs.master.global.service.MilkClassService;
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

public class MemberTypeLoadTask extends Task<List<MemberType>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(MemberTypeLoadTask.class);

    @Override
    protected List<MemberType> call() throws Exception {
        try {
            MemberTypeService service = EmcsAppContext.getContext().getBean(MemberTypeService.class);
            List<MemberType> list = service.findAll();
            return list;
        } catch (Exception e) {
            LOGGER.error("MemberTypes fetch", e);
        }
        return null;
    }
}

