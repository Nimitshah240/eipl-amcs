package com.eipl.amcs.master.account.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.dto.YearClosingDto;
import com.eipl.amcs.master.account.service.FinancialYearService;
import com.eipl.amcs.util.CommonUtil;
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
            return service.saveDto(dto, CommonUtil.setIdentityHeader());


//            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
//            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.FINANCIAL_YEAR;
//
//            ResponseEntity<YearClosingDto> response =
//                    restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(dto), YearClosingDto.class);
//
//            if (response == null || response.getStatusCode() != HttpStatus.CREATED)
//                return null;
//            return  response.getBody() != null?response.getBody():null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
