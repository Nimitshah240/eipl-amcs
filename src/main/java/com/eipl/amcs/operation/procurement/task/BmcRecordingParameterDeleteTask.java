package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.procurement.service.BmcRecordingService;
import javafx.concurrent.Task;

public class BmcRecordingParameterDeleteTask extends Task<Boolean> {
    private final Long code;

    public BmcRecordingParameterDeleteTask(Long code) {
        this.code = code;
    }

    @Override
    protected Boolean call() throws Exception {
        try {
            BmcRecordingService service = EmcsAppContext.getContext().getBean(BmcRecordingService.class);
            service.deleteBmcRecording(code);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
