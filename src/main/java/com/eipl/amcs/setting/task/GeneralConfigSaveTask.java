package com.eipl.amcs.setting.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.setting.model.GeneralConfig;
import com.eipl.amcs.setting.service.GeneralConfigService;
import com.eipl.amcs.util.CommonUtil;
import com.eipl.amcs.utils.ApiJsonUtil;
import javafx.concurrent.Task;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.List;

public class GeneralConfigSaveTask extends Task<Object> {
    private List<GeneralConfig> generalConfigList;

    public GeneralConfigSaveTask(List<GeneralConfig> deviceConfigList) {
        this.generalConfigList = deviceConfigList;
    }

    @Override
    protected Object call() throws Exception {
        try {

            GeneralConfigService service = EmcsAppContext.getContext().getBean(GeneralConfigService.class);
            List<GeneralConfig> list = service.save(generalConfigList, CommonUtil.setIdentityHeader());
            if (list == null || list.isEmpty())
                return null;
            return list;
        } catch (HttpStatusCodeException e) {
            return EmcsAppContext.getContext().getBean(ApiJsonUtil.class).parseJsonString(e.getResponseBodyAsString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
