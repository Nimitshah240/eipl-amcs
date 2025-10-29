package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.procurement.dto.MilkReceiptDto;
import com.eipl.amcs.operation.procurement.model.MilkReceipt;
import com.eipl.amcs.operation.procurement.service.MilkReceiptService;
import com.eipl.amcs.utils.CommonUtils;
import com.eipl.amcs.utils.ApiJsonUtil;
import javafx.concurrent.Task;
import org.springframework.web.client.HttpStatusCodeException;

public class MilkReceiptSaveTask extends Task<Object> {

    private final MilkReceiptDto dto;
    private final short update;

    public MilkReceiptSaveTask(MilkReceiptDto dto, short update) {
        this.dto = dto;
        this.update = update;
    }

    @Override
    protected Object call() throws Exception {
        try {
            MilkReceiptService service = EmcsAppContext.getContext().getBean(MilkReceiptService.class);
            MilkReceipt dtoResult;

            if (this.update == 0) {
                dtoResult = service.save(dto, CommonUtils.setIdentityHeader());
            } else {
                dtoResult = service.update(dto, CommonUtils.setIdentityHeader());
            }
            return dtoResult;
//            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
//            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.MILK_RECEIPT+"/a";
//
//            ResponseEntity<MilkReceiptDto> response = this.update == 0 ?
//                    restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(dto), MilkReceiptDto.class) :
//                    restTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(dto), MilkReceiptDto.class);
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
