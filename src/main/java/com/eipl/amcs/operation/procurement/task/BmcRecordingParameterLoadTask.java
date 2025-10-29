package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.procurement.model.BmcRecording;
import com.eipl.amcs.operation.procurement.service.BmcRecordingService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class BmcRecordingParameterLoadTask extends Task<List<BmcRecording>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(BmcRecordingParameterLoadTask.class);

    @Override
    protected List<BmcRecording> call() throws Exception {
        try {
            BmcRecordingService service = EmcsAppContext.getContext().getBean(BmcRecordingService.class);
            List<BmcRecording> list = service.getAllBmcRecordings();
            if (list == null || list.isEmpty())
                return null;
            return list;
        } catch (Exception e) {
            LOGGER.error("RecordingParameter fetch", e);
        }
        return null;
    }
}
