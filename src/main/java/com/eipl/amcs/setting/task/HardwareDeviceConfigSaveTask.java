package com.eipl.amcs.setting.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.setting.model.HardwareDeviceConfig;
import com.eipl.amcs.setting.service.HardwareDeviceConfigService;
import com.eipl.amcs.utils.CommonUtils;
import com.eipl.amcs.utils.ApiJsonUtil;
import javafx.concurrent.Task;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.List;

public class HardwareDeviceConfigSaveTask extends Task<Object> {
    private final List<HardwareDeviceConfig> deviceConfigList;

    public HardwareDeviceConfigSaveTask(List<HardwareDeviceConfig> deviceConfigList) {
        this.deviceConfigList = deviceConfigList;
    }

    @Override
    protected Object call() throws Exception {
        try {
            HardwareDeviceConfigService service = EmcsAppContext.getContext().getBean(HardwareDeviceConfigService.class);
            return service.saveUpdate(deviceConfigList, CommonUtils.setIdentityHeader());
        } catch (HttpStatusCodeException e) {
            return EmcsAppContext.getContext().getBean(ApiJsonUtil.class).parseJsonString(e.getResponseBodyAsString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
