package com.eipl.amcs.auth.task;

import com.eipl.amcs.auth.dto.IdentityDto;
import com.eipl.amcs.auth.service.IdentityService;
import com.eipl.amcs.config.EmcsAppContext;
import javafx.concurrent.Task;

public class IdentityTask extends Task<IdentityDto> {
    private String dockNumber;
    private String societyCode;
    private String unionCode;

    public IdentityTask(String dockNumber, String societyCode, String unionCode) {
        this.dockNumber = dockNumber;
        this.societyCode = societyCode;
        this.unionCode = unionCode;
    }

    @Override
    protected IdentityDto call() throws Exception {
        try {
            IdentityService service = EmcsAppContext.getContext().getBean(IdentityService.class);
            return service.fetchIdentity(dockNumber, societyCode, unionCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
