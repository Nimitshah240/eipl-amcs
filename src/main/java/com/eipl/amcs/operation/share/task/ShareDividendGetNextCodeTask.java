package com.eipl.amcs.operation.share.task;

import com.eipl.amcs.base.service.NextCodeService;
import com.eipl.amcs.config.EmcsAppContext;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShareDividendGetNextCodeTask extends Task<String> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ShareDividendGetNextCodeTask.class);

    public ShareDividendGetNextCodeTask() {
    }

    @Override
    protected String call() throws Exception {
        try {

            NextCodeService nextCodeService = EmcsAppContext.getContext().getBean(NextCodeService.class);
            String codeI = nextCodeService.getNextCode("ProductSale", "invoiceNo", "code", 6);
            if (codeI == null || codeI.isEmpty()) return null;
            return codeI;

//            String code = MainApp.identityDto.getSociety().getCode()+"/"+MainApp.getFinancialYear().getCode()+"/";
//            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
//            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.PRODUCT_SALE_TO_MEMBER_NUMBER;
//            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url).queryParam(MainApp.identityDto.getSociety().getCode())
//                    .queryParam("code",code);
//            ResponseEntity<String> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, null, String.class);
//            if (response == null || response.getStatusCode() != HttpStatus.OK)
//                return null;
//            LOGGER.info("ProductSale No fetched: {}", response.getBody());
//            return response.getBody();
        } catch (Exception e) {
            LOGGER.error("ProductSale No fetch", e);
        }
        return null;
    }

}
