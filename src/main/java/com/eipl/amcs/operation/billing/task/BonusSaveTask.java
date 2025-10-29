package com.eipl.amcs.operation.billing.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.billing.dto.BonusDto;
import com.eipl.amcs.operation.billing.service.BonusService;
import com.eipl.amcs.utils.CommonUtils;
import com.eipl.amcs.utils.ApiJsonUtil;
import javafx.concurrent.Task;
import org.springframework.web.client.HttpStatusCodeException;

public class BonusSaveTask extends Task<Object> {
    private final BonusDto dto;
    private final short update;

    public BonusSaveTask(BonusDto dto, short update) {
        this.dto = dto;
        this.update = update;
    }


    @Override
    protected Object call() throws Exception {
        try {
            BonusService service = EmcsAppContext.getContext().getBean(BonusService.class);

            if (this.update == 0) {
                service.saveDto(CommonUtils.setIdentityHeader(), dto, (short) 0);
            } else {
                service.updateDto(CommonUtils.setIdentityHeader(), dto);
            }
//            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
//            String url= MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.BONUS;
//            ResponseEntity<BonusDto> response = this.update == 0 ?
//                    restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(dto), BonusDto.class) :
//                    restTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(dto), BonusDto.class);
//
//            if (response == null || response.getStatusCode() != HttpStatus.CREATED)
//                return null;
//            return response.getStatusCode() == HttpStatus.CREATED && response.getBody() != null;
            return true;
        } catch (HttpStatusCodeException e) {
            return EmcsAppContext.getContext().getBean(ApiJsonUtil.class).parseJsonString(e.getResponseBodyAsString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
