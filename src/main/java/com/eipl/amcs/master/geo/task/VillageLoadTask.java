package com.eipl.amcs.master.geo.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.geo.model.SubDistrict;
import com.eipl.amcs.master.geo.model.Village;
import com.eipl.amcs.master.geo.service.VillageService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class VillageLoadTask extends Task<List<Village>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(VillageLoadTask.class);
    private SubDistrict subDistrict;

    public VillageLoadTask() {

    }

    public VillageLoadTask(SubDistrict subDistrict) {
        this.subDistrict = subDistrict;
    }

    @Override
    protected List<Village> call() throws Exception {
        try {
            VillageService service = EmcsAppContext.getContext().getBean(VillageService.class);
            List<Village> list;
            if (subDistrict == null)
                list = service.findAll();
            else
                list = service.findAll(subDistrict.getCode());
            if (list == null || list.isEmpty())
                return null;
            return list;
        } catch (Exception e) {
            LOGGER.error("Villages fetch", e);
        }
        return null;
    }
}

