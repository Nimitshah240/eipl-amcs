package com.eipl.amcs.base.task;

import com.eipl.amcs.base.model.Notification;
import com.eipl.amcs.base.service.NotificationService;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.utils.CommonUtils;
import javafx.concurrent.Task;

import java.util.List;

public class NotificationSaveTask extends Task<List<Notification>> {

    private final List<Notification> dto;

    public NotificationSaveTask(List<Notification> dto) {
        this.dto = dto;
    }


    @Override
    protected List<Notification> call() throws Exception {
        try {
            NotificationService service = EmcsAppContext.getContext().getBean(NotificationService.class);
            List<Notification> list = service.save(dto, CommonUtils.setIdentityHeader());
            if (list == null || list.isEmpty())
                return null;
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
