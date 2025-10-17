package com.eipl.amcs.operation.share.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.service.NextCodeService;
import com.eipl.amcs.config.EmcsAppContext;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShareGetNextCodeTask extends Task<String> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ShareGetNextCodeTask.class);

    public ShareGetNextCodeTask() {
    }

    @Override
    protected String call() throws Exception {
        try {
            NextCodeService nextCodeService = EmcsAppContext.getContext().getBean(NextCodeService.class);
//            TODO - what is code ? How should i get?
            String codes = nextCodeService.getNextCode("Share", "code", MainApp.identityDto.getSociety().getCode(), 5);
            if (codes == null || codes.isEmpty()) return null;
            return codes;

//            String code = MainApp.identityDto.getSociety().getCode();
//            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
//            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.SHARE+"/next-code";
//            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url).queryParam(MainApp.identityDto.getSociety().getCode())
//                    .queryParam("code",code);
//            ResponseEntity<String> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, null, String.class);
//            if (response == null || response.getStatusCode() != HttpStatus.OK)
//                return null;
//            LOGGER.info("Share No fetched: {}", response.getBody());
//            return response.getBody();
        } catch (Exception e) {
            LOGGER.error("Share No fetch", e);
        }
        return null;
    }

}
