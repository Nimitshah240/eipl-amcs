package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.procurement.dto.MilkDispatchDto;
import com.eipl.amcs.operation.procurement.model.MilkDispatch;
import com.eipl.amcs.operation.procurement.service.MilkDispatchService;
import com.eipl.amcs.util.CommonUtil;
import com.eipl.amcs.utils.ApiJsonUtil;
import javafx.concurrent.Task;
import org.springframework.web.client.HttpStatusCodeException;

public class MilkDispatchSaveTask extends Task<Object> {

    private final MilkDispatchDto dto;
    private final short update;

    public MilkDispatchSaveTask(MilkDispatchDto dto, short update) {
        this.dto = dto;
        this.update = update;
    }

    @Override
    protected Object call() throws Exception {
        try {

            MilkDispatchService service = EmcsAppContext.getContext().getBean(MilkDispatchService.class);
            MilkDispatch dtoResult;

            if (this.update == 0) {
                dtoResult = service.save(dto, CommonUtil.setIdentityHeader());
            } else {
                dtoResult = service.update(dto, CommonUtil.setIdentityHeader());
            }

            return dtoResult;

//            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
//            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.MILK_DISPATCH;
//
//            ResponseEntity<MilkDispatchDto> response = this.update == 0 ?
//                    restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(dto), MilkDispatchDto.class) :
//                    restTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(dto), MilkDispatchDto.class);
//
//            if (response == null || response.getStatusCode() != HttpStatus.CREATED)
//                return null;
//            return response.getBody();
        } catch (HttpStatusCodeException e) {
            return EmcsAppContext.getContext().getBean(ApiJsonUtil.class).parseJsonString(e.getResponseBodyAsString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
