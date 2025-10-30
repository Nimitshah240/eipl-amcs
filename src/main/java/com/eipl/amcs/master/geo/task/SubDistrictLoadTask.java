package com.eipl.amcs.master.geo.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.geo.model.District;
import com.eipl.amcs.master.geo.model.SubDistrict;
import com.eipl.amcs.master.geo.service.SubDistrictService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SubDistrictLoadTask extends Task<List<SubDistrict>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(SubDistrictLoadTask.class);
    private District district;

    public SubDistrictLoadTask() {
    }

    public SubDistrictLoadTask(District district) {
        this.district = district;
    }

    @Override
    protected List<SubDistrict> call() throws Exception {
        try {
            SubDistrictService service = EmcsAppContext.getContext().getBean(SubDistrictService.class);
            List<SubDistrict> list;
            if (district == null)
                list = service.findAll();
            else
                list = service.findAll(district.getCode());
            if (list == null || list.isEmpty())
                return null;
            return list;
        } catch (Exception e) {
            LOGGER.error("SubDistricts fetch", e);
        }
        return null;
    }
}

