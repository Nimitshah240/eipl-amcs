package com.eipl.amcs.operation.billing.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.billing.dto.BonusDto;
import com.eipl.amcs.operation.billing.service.BonusService;
import com.eipl.amcs.util.CommonUtil;
import com.eipl.amcs.utils.ApiJsonUtil;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

public class BonusSaveTask extends Task<Object> {
    private BonusDto dto;
    private short update;

    public BonusSaveTask(BonusDto dto, short update) {
        this.dto = dto;
        this.update = update;
    }


    @Override
    protected Object call() throws Exception {
        try {
            BonusService service = EmcsAppContext.getContext().getBean(BonusService.class);

            if(this.update == 0){
                service.saveDto(CommonUtil.setIdentityHeader(),dto,(short)0);
            }else{
                service.updateDto(CommonUtil.setIdentityHeader(),dto);
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
