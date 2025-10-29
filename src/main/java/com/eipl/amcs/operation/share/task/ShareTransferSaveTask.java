package com.eipl.amcs.operation.share.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.share.model.Share;
import com.eipl.amcs.operation.share.service.ShareService;
import com.eipl.amcs.utils.CommonUtils;
import com.eipl.amcs.utils.ApiJsonUtil;
import javafx.concurrent.Task;
import org.springframework.web.client.HttpStatusCodeException;

public class ShareTransferSaveTask extends Task<Object> {
    private final Share dto;
    private final short update;

    public ShareTransferSaveTask(Share dto, short update) {
        this.dto = dto;
        this.update = update;
    }

    @Override
    protected Object call() throws Exception {
        try {
            ShareService service = EmcsAppContext.getContext().getBean(ShareService.class);
            if (this.update == 0) {
                service.save(dto, CommonUtils.setIdentityHeader());
            }
//            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
//            String url;
//            ResponseEntity<Share> response;
//                    if(this.update == 0) {
//                        url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.SHARE;
//                        response = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(dto), Share.class);
//                    }else {
//                        url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.SHARE + "/{code}";
//                        Map<String, Object> uriVariables = new HashMap<>();
//                        uriVariables.put("code", dto.getCode());
//                        response = restTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(dto), Share.class, uriVariables);
//                    }
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
