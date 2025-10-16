package com.eipl.amcs.master.account.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.model.SubLedgerLedgerConfig;
import com.eipl.amcs.master.account.service.SubLedgerLedgerConfigService;
import com.eipl.amcs.master.inventory.task.ProductPurchaseRateByProductTask;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SubLedgerLedgerConfigBySubLedgerTypeLoadTask extends Task<List<SubLedgerLedgerConfig>> {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(ProductPurchaseRateByProductTask.class);

    private final Integer code;

    public SubLedgerLedgerConfigBySubLedgerTypeLoadTask(Integer code) {
        this.code = code;
    }

    @Override
    protected List<SubLedgerLedgerConfig> call() throws Exception {
        try {
            SubLedgerLedgerConfigService service = EmcsAppContext.getContext().getBean(SubLedgerLedgerConfigService.class);
            List<SubLedgerLedgerConfig> list = service.findBySubLedgerType(code);
            if (list == null || list.isEmpty())
                return null;
            return list;

//			RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
//			String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null)
//					+ AppConstant.UrlPath.SUB_LEDGER_LEDGER_CONFIG + "/by-subledger-type";
//			UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(url).queryParam("code",
//					code);
//			ResponseEntity<SubLedgerLedgerConfig[]> response = restTemplate.exchange(uriComponentsBuilder.toUriString(),
//					HttpMethod.GET, null, SubLedgerLedgerConfig[].class);
//			if (response == null || response.getStatusCode() != HttpStatus.OK)
//				return null;
//			LOGGER.info("Config fetched: {}", response.getBody());
//			return response.getBody()!=null?Arrays.asList(response.getBody()):null;
        } catch (Exception e) {
            LOGGER.error("Config fetch", e);
        }
        return null;
    }
}
