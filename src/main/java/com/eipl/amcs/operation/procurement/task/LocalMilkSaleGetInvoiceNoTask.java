package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.service.NextCodeService;
import com.eipl.amcs.config.EmcsAppContext;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LocalMilkSaleGetInvoiceNoTask extends Task<String> {
    private static final Logger LOGGER = LoggerFactory.getLogger(LocalMilkSaleGetInvoiceNoTask.class);

//	private final String code;

//	public ProductReceiptGetNextCodeTask(String code) {
//		this.code = code;
//	}

    public LocalMilkSaleGetInvoiceNoTask() {
    }

    @Override
    protected String call() throws Exception {
        try {
            String code = MainApp.identityDto.getSociety().getCode() + "/" + MainApp.getFinancialYear().getCode() + "/";
            NextCodeService nextCodeService = EmcsAppContext.getContext().getBean(NextCodeService.class);
            String codes = nextCodeService.getNextCode("LocalMilkSale", "invoiceNo", code, 5);
            if (codes == null || codes.isEmpty())
                return null;
            return codes;

//            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
//            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.LOCAL_MILK_SALE + "/fetchInvoiceNo";
//            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url).queryParam(MainApp.identityDto.getSociety().getCode())
//                    .queryParam("code", code);
//            ResponseEntity<String> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, null, String.class);
//            if (response == null || response.getStatusCode() != HttpStatus.OK)
//                return null;
//            LOGGER.info("LocalMilkSale InvoiceNo fetched: {}", response.getBody());
//            return response.getBody();
        } catch (Exception e) {
            LOGGER.error("LocalMilkSale InvoiceNo fetched: {}", e);
        }
        return null;
    }

}
