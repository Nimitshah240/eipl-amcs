package com.eipl.amcs.master.account.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.dto.YearClosingDto;
import com.eipl.amcs.master.account.service.FinancialYearService;
import com.eipl.amcs.utils.CommonUtils;
import javafx.concurrent.Task;

public class YearClosingDtoSaveTask extends Task<Object> {

    private final YearClosingDto dto;

    public YearClosingDtoSaveTask(YearClosingDto dto) {
        this.dto = dto;
    }

    @Override
    protected YearClosingDto call() throws Exception {
        try {
            FinancialYearService service = EmcsAppContext.getContext().getBean(FinancialYearService.class);
            return service.saveDto(dto, CommonUtils.setIdentityHeader());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
