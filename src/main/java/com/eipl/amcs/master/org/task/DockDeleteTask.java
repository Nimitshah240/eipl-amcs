package com.eipl.amcs.master.org.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.org.model.Dock;
import com.eipl.amcs.master.org.service.DockService;
import com.eipl.amcs.utils.CommonUtils;
import javafx.concurrent.Task;

import java.util.Optional;

public class DockDeleteTask extends Task<Boolean> {
    private final String dockNo;

    public DockDeleteTask(String dockNo) {
        this.dockNo = dockNo;
    }

    @Override
    protected Boolean call() throws Exception {
        try {
            DockService service = EmcsAppContext.getContext().getBean(DockService.class);
            Optional<Dock> dockData = service.findById(dockNo);
            if (dockData == null || !dockData.isPresent())
                return null;
            service.delete(dockData.get(), CommonUtils.setIdentityHeader());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
